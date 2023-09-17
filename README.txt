1. Методы используемые для фильтрации пользователя, почему выбрали
именно эти методы фильтрации:
isEmulator(): Этот метод определяет, запущено ли приложение на эмуляторе или реальном устройстве.
isLocationAvailable(Context): Этот метод проверяет, доступна ли служба геолокации на устройстве, а именно, включены ли GPS или NETWORK провайдеры.
getPublicIpAddress() и checkIp(): Эти методы получают публичный IP-адрес устройства пользователя и проверяют его. В настоящее время в методе checkIp() просто проверяется, является ли IP пустым или нет, но, как отмечено в комментарии, можно добавить проверки на "допустимые" или "недопустимые" IP.
 проверка !networkController.isConnected проверяет наличие интернет-соединения.
2. Какие свои методы фильтрации или уникализации кода придумали и
добавили:
Добавил метод проверки isEmulator() и управление серой частью удаленно через Firebase
3. Приложить инструкцию как включить и отключить фильтрацию в коде
для оценки обеих частей приложения:
Для того, чтобы подключить или отключить фильтрацию пользователя нужно зарегестрировать приложение на Firebase, добавить google-services.json раскоментировать код в MainActivity, build.gradle(уровня модуля) и build.gradle(уровня проекта). Нужно настроить Firebase Realtime Database, в разделе Rules изменить параметр read на true и в Data добавить ключ isOpened со значением true. Когда isOpened:true - будет активирована серая, false - серая часть отключена (выключать на момент модерации в GooglePlay) и будет открыватся заглушка.
4. Какие технологии использовали для разработки своего приложения:
Kotlin
Android SDK
AndroidX (AppCompatActivity, Fragment и др.)
Android Lifecycle (lifecycleScope и т. д.)
Coroutines (suspend, launch, withContext и др.)
SharedPreferences
WebView (WebViewClient, WebChromeClient)
AlertDialog
WebResourceRequest
MediaStore
FileProvider
CoroutineScope
Dispatchers
LocationManager (GPS_PROVIDER, NETWORK_PROVIDER)
ActivityResultLauncher и ActivityResultContracts
Intent (ACTION_VIEW, ACTION_GET_CONTENT, ACTION_IMAGE_CAPTURE и др.)
ValueCallback
Delegates (notNull и др.)