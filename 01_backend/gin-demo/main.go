package main

import (
	"fmt"

	"gorm.io/driver/mysql"
	"gorm.io/gorm"
)

type UserService struct {
	Name string
}

// 值接收者
func (s UserService) ValueMethod() {
	fmt.Println("ValueMethod:", s.Name)
}

// 指標接收者
func (s *UserService) PointerMethod() {
	fmt.Println("PointerMethod:", s.Name)
}

func main() {
	v := UserService{Name: "VALUE"}
	p := &UserService{Name: "POINTER"}

	fmt.Println("---- v (value) 呼叫 ----")
	v.ValueMethod()   // ①
	v.PointerMethod() // ② Go 自動補 & >>

	fmt.Println("---- p (pointer) 呼叫 ----")
	p.ValueMethod()   // ③ Go 自動補 *
	p.PointerMethod() // ④
}

func InitDB() (*gorm.DB, error) {
	dsn := "user:password@tcp(127.0.0.1:3306)/dbname?charset=utf8mb4&parseTime=True&loc=Local"
	return gorm.Open(mysql.Open(dsn), &gorm.Config{})
}
