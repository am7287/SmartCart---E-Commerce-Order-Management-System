$ErrorActionPreference = 'Stop'

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21'
$env:MYSQL_USERNAME = if ($env:MYSQL_USERNAME) { $env:MYSQL_USERNAME } else { 'root' }

if (-not $env:MYSQL_PASSWORD) {
    $env:MYSQL_PASSWORD = Read-Host 'Enter MySQL password'
}

$logDir = Join-Path $root 'logs'

New-Item -ItemType Directory -Force -Path $logDir | Out-Null

$services = @(
    @{ Name = 'config-server'; Port = 8888; Wait = 15 },
    @{ Name = 'discovery-server'; Port = 8761; Wait = 18 },
    @{ Name = 'user-service'; Port = 8081; Wait = 15 },
    @{ Name = 'product-service'; Port = 8082; Wait = 15 },
    @{ Name = 'order-service'; Port = 8083; Wait = 15 },
    @{ Name = 'api-gateway'; Port = 8080; Wait = 15 }
)

Write-Host 'Starting SmartCart services...'

foreach ($service in $services) {
    $existing = Get-NetTCPConnection -LocalPort $service.Port -ErrorAction SilentlyContinue |
        Where-Object { $_.State -eq 'Listen' }

    if ($existing) {
        Write-Host "$($service.Name) already appears to be running on port $($service.Port)."
        continue
    }

    Write-Host "Starting $($service.Name) on port $($service.Port)..."

    Start-Process `
        -FilePath 'mvn' `
        -ArgumentList @('-pl', $service.Name, 'spring-boot:run') `
        -WorkingDirectory $root `
        -WindowStyle Hidden `
        -RedirectStandardOutput (Join-Path $logDir "$($service.Name).out.log") `
        -RedirectStandardError (Join-Path $logDir "$($service.Name).err.log")

    Start-Sleep -Seconds $service.Wait
}

Write-Host ''
Write-Host 'SmartCart startup check:'

foreach ($service in $services) {
    $running = Get-NetTCPConnection -LocalPort $service.Port -ErrorAction SilentlyContinue |
        Where-Object { $_.State -eq 'Listen' }

    if ($running) {
        Write-Host "[OK] $($service.Name) is listening on port $($service.Port)"
    } else {
        Write-Host "[CHECK LOGS] $($service.Name) is not listening on port $($service.Port)"
    }
}

Write-Host ''
Write-Host 'Use API Gateway at http://localhost:8080'
Write-Host 'Use Eureka Dashboard at http://localhost:8761'
