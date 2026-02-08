package core

import (
	"database/sql"
	"fmt"

	_ "github.com/go-sql-driver/mysql"
)

type DB struct {
	conn *sql.DB
}

// NewDB creates a new database connection
func NewDB(datasource string) (*DB, error) {
	conn, err := sql.Open("mysql", datasource)
	if err != nil {
		return nil, fmt.Errorf("failed to open database: %w", err)
	}

	// Test connection
	err = conn.Ping()
	if err != nil {
		return nil, fmt.Errorf("failed to connect to database: %w", err)
	}

	return &DB{
		conn: conn,
	}, nil
}

func (d *DB) GetConn() *sql.DB {
	return d.conn
}

// Close closes the database connection
func (d *DB) Close() error {
	return d.conn.Close()
}

