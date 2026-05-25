$patterns = @(
    'com.smartcart.configserver.ConfigServerApplication',
    'com.smartcart.discoveryserver.DiscoveryServerApplication',
    'com.smartcart.userservice.UserServiceApplication',
    'com.smartcart.productservice.ProductServiceApplication',
    'com.smartcart.orderservice.OrderServiceApplication',
    'com.smartcart.apigateway.ApiGatewayApplication'
)

$stopped = 0
$javaProcesses = Get-CimInstance Win32_Process | Where-Object { $_.Name -eq 'java.exe' }

foreach ($process in $javaProcesses) {
    foreach ($pattern in $patterns) {
        if ($process.CommandLine -like "*$pattern*") {
            Stop-Process -Id $process.ProcessId -Force
            Write-Host "Stopped process $($process.ProcessId): $pattern"
            $stopped++
            break
        }
    }
}

if ($stopped -eq 0) {
    Write-Host 'No SmartCart services were running.'
} else {
    Write-Host "Stopped $stopped SmartCart service process(es)."
}
