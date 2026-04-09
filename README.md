# SealDice2MC

SealDice2MC是一个服务端模组，支持将SealDice（一个开源骰子机器人）连接到Minecraft服务器，支持fabric和(Neo)Forge服务器，允许玩家直接从游戏中与SealDice进行交互，发送消息和掷骰命令。

## MC服务器说明

- 该模组在服务端开放一个端口，供SealDice机器人连接。
- 连接端口可以通过`/sealport <port>`命令更改。该命令需要OP权限，也可以从服务器控制台执行。或者也可以修改`config/sealdice2mc.properties`文件中的`websocket.port`属性来更改端口。
- 玩家可以直接在聊天栏发送消息或使用`/sealdice <message>`命令来与SealDice机器人交互。`/sealdice`命令视为向海豹私聊发送了一条消息 但是命令方块和控制台使用该指令会被视为公屏发送消息。

## 海豹骰对接说明

- 在海豹的帐号添加页面选择 Minecraft服务器
- 按照 ip:端口 的格式填入输入框内，然后点下一步

*Logo来自[SealDice](https://dice.weizaima.com/)的官方logo。*