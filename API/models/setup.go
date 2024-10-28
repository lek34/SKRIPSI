package models

import (
	"fmt"
	"os"
	"time"

	"github.com/joho/godotenv"
	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

var DB *gorm.DB

func ConnectDatabase() {
	// Connect to MySQL server without specifying a database
	err := godotenv.Load(".env")
	if err != nil {
		panic(err)
	}
	dsn := os.Getenv("DB_USERNAME") + ":" + os.Getenv("DB_PASSWORD") + "@tcp(" + os.Getenv("DB_HOST") + ":" + os.Getenv("DB_PORT") + ")/?parseTime=true"
	db, err := gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		panic("failed to connect to MySQL")
	}

	// Check if the database exists, and create it if it doesn't
	createDatabaseIfNotExists(db, "futurefarmerapi")

	// Connect to the `futurefarmerapi` database
	dsn = os.Getenv("DB_USERNAME") + ":" + os.Getenv("DB_PASSWORD") + "@tcp(" + os.Getenv("DB_HOST") + ":" + os.Getenv("DB_PORT") + ")/futurefarmerapi?parseTime=true"
	db, err = gorm.Open(mysql.Open(dsn), &gorm.Config{})
	if err != nil {
		panic("failed to connect to futurefarmerapi database")
	}

	// Perform auto migrations
	err = db.AutoMigrate(&User{}, &LevelConfig{}, &SensorData{}, &RelayStatus{}, &RelayConfig{}, &RelayHistory{}, &Plant{})
	if err != nil {
		panic("failed to migrate database")
	}
	// db.Migrator().DropTable(&User{}, &LevelConfig{}, &SensorData{}, &RelayStatus{}, &RelayConfig{}, &RelayHistory{}, &Plant{})
	// db.AutoMigrate(&User{})
	// db.AutoMigrate(&SensorData{})
	// db.AutoMigrate(&RelayStatus{})
	// db.AutoMigrate(&RelayConfig{})
	// db.AutoMigrate(&RelayHistory{})
	// db.AutoMigrate(&LevelConfig{})
	// db.AutoMigrate(&Plant{})

	// Assign the connected DB to the global variable
	DB = db

	// Create initial data if necessary
	initializeDatabaseData()
}
func initializeDatabaseData() {
	var count int64

	// Check if RelayStatus table is empty
	DB.Model(&RelayStatus{}).Count(&count)
	if count == 0 {
		createRelayStatus()
	}

	// Check if LevelConfig table is empty
	DB.Model(&LevelConfig{}).Count(&count)
	if count == 0 {
		createLevelConfig()
	}

	// Check if RelayConfig table is empty
	DB.Model(&RelayConfig{}).Count(&count)
	if count == 0 {
		createRelayConfig()
	}
}
func createDatabaseIfNotExists(db *gorm.DB, dbName string) {
	sql := fmt.Sprintf("CREATE DATABASE IF NOT EXISTS %s", dbName)
	if err := db.Exec(sql).Error; err != nil {
		panic(fmt.Sprintf("failed to create database %s: %v", dbName, err))
	}
}

func createRelayStatus() {
	relayStatus := RelayStatus{
		Ph_up:     0,
		Ph_down:   0,
		Nut_a:     0,
		Nut_b:     0,
		Fan:       0,
		Light:     0,
		CreatedAt: time.Now(),
	}

	// Insert the new record into the database
	result := DB.Create(&relayStatus)
	if result.Error != nil {
		panic("failed to insert relay status record")
	}
}

func createLevelConfig() {
	levelConfig := LevelConfig{
		Ph_low:           6.5,
		Ph_high:          7.0,
		Tds:              100,
		Temperature_low:  33,
		Temperature_high: 40,
		Humidity:         70,
	}

	// Insert the new record into the database
	result := DB.Create(&levelConfig)
	if result.Error != nil {
		panic("failed to insert relay status record")
	}
}

func createRelayConfig() {
	relayConfig := RelayConfig{
		Ph_up:   20,
		Ph_down: 20,
		Nut_A:   20,
		Nut_B:   20,
		Fan:     20,
		Light:   20,
		IsSync:  1,
	}

	// Insert the new record into the database
	result := DB.Create(&relayConfig)
	if result.Error != nil {
		panic("failed to insert relay status record")
	}
}
