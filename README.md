# Hummingbird Game Engine
Hummingbird Game Engine, a high efficiency 2D game engine for Android platform.

This game engine is designed based on the idea of batch rendering. We refer the idea from classic desktop computer 2D Game Engine ["Haaf's Game Engine (HGE),"](https://sourceforge.net/projects/hge/) and some other similar engines, such as [Cocos2d-x](http://www.cocos2d-x.org/). The basic graphic renders in conventional windows PC lay on DirectX. The motivation of the development is to design a fast 2D engine on Android platform. The traditional graphic rendering on Android is not fast enough for video games. We studied the algorithms in desktop PC engines, then designed an engine based on OpenGL ES, that can efficiently running on Android platform.

A comparision of performance for the Hummingbird Game Engine and other Engines can be found in my another repository ["SpriteTest."](https://github.com/minicpp/SpriteTest) We compare the performance in terms of FPS under different number of sprites drawn on the screen tested on 3 engines: Hummingbird, Cocos2d-x, and Android:

[![Compare 3 different drawing methods, Hummingbird, Cocos2d-x, and Android Canvas](https://img.youtube.com/vi/agHmpYKi2_M/0.jpg)](https://youtu.be/agHmpYKi2_M?list=PLAkmswnok1in9Qq1BcDWr0W_9EbqbJPx8)

The Hummingbird game engine has been used in the development of 2 published games on Android.
-AdventRush, a bullets curtain game. This game shows the power of the engine in rendering a large number of dynamic bullets on the screen smoothly, even on low-end mobile devices.

[![AdventRush](https://img.youtube.com/vi/_bA8sj6XnMA/0.jpg)](https://youtu.be/_bA8sj6XnMA?list=PLAkmswnok1in9Qq1BcDWr0W_9EbqbJPx8)

-Kapow, a social networking game, used to trick your friends. This game shows the efficiency of the engine in drawing multiple background layouts and animations.

[![Kapow](https://img.youtube.com/vi/ClTcYpVR_QQ/0.jpg)](https://youtu.be/ClTcYpVR_QQ?list=PLAkmswnok1in9Qq1BcDWr0W_9EbqbJPx8)
