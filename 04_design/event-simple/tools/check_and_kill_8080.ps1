# check_and_kill_8080.ps1

# �ˬd�O�_�H�޲z���B��A�p�G���O�N���s�Ұʦۤv
If (-Not ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] "Administrator"))
{
    Write-Warning "���}���ݭn�޲z���v���A���b�H�޲z����������..."
    Start-Process powershell -ArgumentList "-NoProfile -ExecutionPolicy Bypass -File `"$PSCommandPath`"" -Verb RunAs
    Exit
}

# ��X���� 8080 �� PID
$port = 8080
$processInfo = netstat -ano | findstr ":$port"

if ($processInfo) {
    Write-Host "�o�{���� 8080 ���{�ǡA�ǳƲפ�..."
    
    # �� PID (netstat �̫�@��)
    $killPID = ($processInfo -split '\s+')[-1]
    
    # ��ܵ{�ǦW��
    $task = tasklist /FI "PID eq $killPID"
    Write-Host "���b�פ� PID $killPID ���{��:"
    Write-Host $task

    # �פ�{��
    taskkill /PID $killPID /F
    Write-Host "�w���\�פ� PID $killPID"
} else {
    Write-Host "8080 �S���Q�e�ΡA�i�H�w���Ұ� Spring Boot"
}

# �Ȱ��A���ݥΤ�����N��
Pause
