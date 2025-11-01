# check_and_kill_8080.ps1

# 檢查是否以管理員運行，如果不是就重新啟動自己
If (-Not ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] "Administrator"))
{
    Write-Warning "此腳本需要管理員權限，正在以管理員身份重啟..."
    Start-Process powershell -ArgumentList "-NoProfile -ExecutionPolicy Bypass -File `"$PSCommandPath`"" -Verb RunAs
    Exit
}

# 找出佔用 8080 的 PID
$port = 8080
$processInfo = netstat -ano | findstr ":$port"

if ($processInfo) {
    Write-Host "發現佔用 8080 的程序，準備終止..."
    
    # 取 PID (netstat 最後一欄)
    $killPID = ($processInfo -split '\s+')[-1]
    
    # 顯示程序名稱
    $task = tasklist /FI "PID eq $killPID"
    Write-Host "正在終止 PID $killPID 的程序:"
    Write-Host $task

    # 終止程序
    taskkill /PID $killPID /F
    Write-Host "已成功終止 PID $killPID"
} else {
    Write-Host "8080 沒有被占用，可以安全啟動 Spring Boot"
}

# 暫停，等待用戶按任意鍵
Pause
