# kotlin Asynk Resources

![](https://travis-ci.org/kotlin-es/kotlin-asynk-resources.svg?branch=master) ![](https://img.shields.io/badge/kotlin-1.0.6-blue.svg) ![](https://img.shields.io/badge/maven-3.3.9-orange.svg)

This small application is used to download asynchronous resources of roms of the gameBoy emulator.

Project : https://github.com/vicboma1/emulators/tree/master/gameboyclassic


# Api

##  vararg Pair<String,String>

```java
    val listCompletable = DownloadAsynk.submit(
              Pair("https://raw.githubusercontent.com/kotlin-es/kotlin-asynk-resources/master/src/main/resource/AeroStar(J)%5B!%5D.zip","./src/Aero-Star.zip"),
              Pair("https://raw.githubusercontent.com/kotlin-es/kotlin-asynk-resources/master/src/main/resource/Alien3(J)%5B!%5D.zip", "Alien-3.zip"),
              Pair("https://raw.githubusercontent.com/kotlin-es/kotlin-asynk-resources/master/src/main/resource/SuperMarioLand2-6GoldenCoins(UE)(V1.2)%5B!%5D.zip","Super-Mario-Land-2-6-Golden-Coins.zip")
           )
    )
```

##  Pair<String,String>


```java
    val listCompletable = DownloadAsynk.submit(
              Pair("https://raw.githubusercontent.com/kotlin-es/kotlin-asynk-resources/master/src/main/resource/AeroStar(J)%5B!%5D.zip","./src/Aero-Star.zip"),
          )
    )
```

![](https://github.com/kotlin-es/kotlin-asynk-resources/blob/master/src/main/resource/Asynk-Resources.gif)
