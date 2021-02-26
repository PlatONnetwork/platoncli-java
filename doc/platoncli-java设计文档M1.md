## platoncli-java设计文档M1

### 设计目标

- 代码简单，工具易用；
- 功能完善，互相独立；
- 易于维护，便于扩展；
- 兼容性强，兼容多网；

### 依赖库

platoncli-java使用的依赖库主要包括两大部分，分别为外部依赖库和内部依赖库，其中外部依赖库主要包括命令行处理库*jcommander*和日志处理库*slf4j*，内部依赖库为基于*alaya*网络的*java sdk*；

- **jcommander**
    - 注解驱动：它的核心功能命令行参数定义是基于注解的，这也是我选择用它的主要原因。我们可以轻松做到命令行参数与属性的映射，属性除了是String类型，还可以是Integer、boolean，甚至是File、集合类型。
    - 功能丰富：它同时支持文章开头的两种命令行风格，并且提供了输出帮助文档的能力(usage())，还提供了国际化的支持。
    - 高度扩展：基于java的jcommander可以轻易实现扩展性功能。
- **slf4j**
    - 可自定义日志级别，日志格式等；
    - 简单易用；
- **java sdk**
    - 基于alaya网络的alaya-core。主要用于钱包的创建，rpc命令的调用，交易的签名等，通过代码重构，实现同时兼容platon和alaya等不同hrp的网络；

### 日志模块设计

对应配置文件为：log4j.properties

- 日志级别

    - 由低到高：debug，info，warn，error；

    - 默认日志级别：info；


- 日志内容格式

  > {timestamp} {logLevel}  
  > Command: {cmd}  
  > Result: {msg}

如：

  ```log
2021-02-26 15:35:14 INFO  
Command: java -jar platon-jcli-jar-with-dependencies.jar delegate_getDelegateReward -p
param/delegate_getDelegateReward.json  
Result: Data:
[{
"nodeId":"89ca7ccb7fab8e4c8b1b24c747670757b9ef1b3b7631f64e6ea6b469c5936c501fcdcfa7fef2a77521072162c1fc0f8a1663899d31ebb1bc7d00678634ef746c",
"reward":0,
"stakingNum":522944 }
]

```

- 日志存放

  > 存放路径：当前jar所在目录的logs文件夹下
  >
  > log文件名称：platoncli-java.log.YYYY-MM-DD，如：platoncli-java.log.2021-01-20


- 保存日志接口

    - 接口名：Logger.{level}
      > - level：日志级别，如debug，info，warn，error；

    - 参数：msg
      > - msg：操作信息

### 命令概要设计

cli命令主要分为如下三种模式：

#### 1. 帮助和版本号

##### 帮助

```shell
java -jar platoncli-java-jar-with-dependencies.jar --help/-help
```

--help/-help为帮助命令，显示cli的子模块名和命令名，以及对应的帮助说明，如下：

```log
Command: java -jar platoncli-java-jar-with-dependencies.jar -help
Result: Usage: java -jar platoncli-java-jar-with-dependencies.jar [options] [command] 
      [command options]
  Options:
    --config, -config, -c
      指定交易发送的ip和端口的配置文件，全局配置network文件中若配置，可以通过指定配置中名称获取ip和prot，若未填写network配置，而目录下没有节点，则报错；若没有节点，但是有多个配置节点，提示用户指明交易发送节点
      Default: config/node_config.json
    --help, -help
      帮助
    --version, -version, -v
      platon-jcli 版本号
      Default: false
  Commands:
    staking_getCandidateList      查询所有实时候选人列表
      Usage: staking_getCandidateList [options]
        Options:
          --config, -config, -c
            指定交易发送的ip和端口的配置文件，全局配置network文件中若配置，可以通过指定配置中名称获取ip和prot，若未填写network配置，而目录下没有节点，则报错；若没有节点，但是有多个配置节点，提示用户指明交易发送节点
            Default: config/node_config.json
          --help, -help
            帮助

......
此处省略部分打印信息
```

##### 版本号

```shell
java -jar platoncli-java-jar-with-dependencies.jar --version/-v
```

--version/-v为当前工具的版本，包括版本号，commitid和发布工具的时间戳；从发版的version文件读取，如下：

```log
2021-02-26 15:48:53 INFO  
Command: java -jar platoncli-java-jar-with-dependencies.jar -v
Result: version: 0.1.0
revision: 2e4a69d676e15b47ae4544e04c77cbd5c9bd8f65
timestamp: 2021-02-26 15:48:53
```

#### 2. command命令模式

```shell
java -jar platoncli-java-jar-with-dependencies.jar staking_getStakingReward
```

> 该命令的功能是查询当前结算周期的质押奖励。其余的命令可以通过help查询得到。

命令清单如下：  
**委托模块**

命令名称| 功能描述
:---:|:---:
delegate_new|委托
delegate_getDelegateReward|查询账户在各节点未提取委托奖励
delegate_getRelatedListByDelAddr|查询当前账户地址所委托的节点的NodeId和质押Id
delegate_unDelegate|减持/撤销委托
delegate_withdrawDelegateReward|提取委托奖励

**治理模块**

命令名称| 功能描述
:---:|:---:
government_checkDoubleSign|查询节点是否已被举报过多签
government_declareVersion|版本声明
government_getAccuVerifiersCount|查询提案的累计可投票人数
government_getActiveVersion|查询节点的链生效版本
government_getGovernParamValue|查询最新的治理参数值
government_getProposal|根据提案id查询提案信息
government_getTallyResult|查询提案结果
government_listGovernParam|查询治理参数列表
government_listProposal|查询提案列表
government_reportDoubleSign|举报双签
government_submitProposal|提交参数/升级/取消提案
government_vote|提案投票


**查询模块**

命令名称| 功能描述
:---:|:---:
query_blockNumber|查询当前最高块高查询当前最高块高
query_getAvgPackTime|查询打包区块的平均时间
query_getBlockByHash|根据区块hash查询区块信息
query_getBlockByNumber|根据区块块高查询区块信息
query_getPackageReward|查询当前结算周期的区块奖励

**交易模块**

命令名称| 功能描述
:---:|:---:
tx_getTransactionReceipt|根据交易hash查询交易信息
tx_getTransaction|根据交易hash查询交易
tx_sendOffline|发送已签名交易数据
tx_transfer|发送交易

> 可通过如下命令查看命令的帮助信息。
> ```shell
> java -jar platoncli-java-jar-with-dependencies.jar [command] --help
> ```
例如：
```log
2021-02-26 16:01:37 INFO  
Command: java -jar platoncli-java-jar-with-dependencies.jar delegate_new -help
Result: Usage: java -jar platon-jcli-jar-with-dependencies.jar delegate_new [options]
  Options:
  * --address, -address, -d
      发送交易地址或者名称.json
    --config, -config, -c
      指定交易发送的ip和端口的配置文件，全局配置network文件中若配置，可以通过指定配置中名称获取ip和prot，若未填写network配置，而目录下没有节点，则报错；若没有节点，但是有多个配置节点，提示用户指明交易发送节点
      Default: config/node_config.json
    --fast, -fast, -f
      是否使用快速发送功能，默认不使用
      Default: false
    --gasLimit, -gasLimit
      gas用量限制
      Default: 100000
    --gasPrice, -gasPrice
      gas价格
      Default: 1000000000
    --help, -help
      帮助
    --offline, -o
      在线交易或者离线交易. 不输入默认为在线交易, 并生成二维码图片放置在桌面上，提供ATON离线扫码签名
      Default: false
  * --param, -param, -p
      交易参数json字符串，或者交易参数json文件路径
    --template, -template, -t
      查看委托交易参数模板，与其他参数共存没有效果，单独执行查看
```

### 框架设计

**为了方便扩展和维护，采用每个子模块对应src/submodule目录下的一个子目录，每个命令对应一个java文件的方式，模块名和对应的子目录名称相同，命令名和应的文件名相同，子模块之间和命令之间相互独立，可自由的增加和删除**  

代码结构设计如下：

> pom.xml: maven构建配置文件
> 
> src/main/java/.../Main.java：主程序入口  
> 
> src/main/java/.../contractx：存放了重构的合约类
> 
> src/main/java/.../converter：存放命令参数的转换器
> 
> src/main/java/.../model：存放一些POJO用于正反序列化
> 
> src/main/java/.../service：存放重构的HttpService，可用于快速发送交易
> 
> src/main/java/.../template：存放命令的参数模板
> 
> src/main/java/.../util：存放常用工具类以及合约交易工具
> 
> src/main/java/.../validator：存放命令参数的格式校验器
> 
> src/main/resources：保存了打印日志的配置文件
>
>  src/main/java/.../submodule： 命令代码存放路径
>
> ​ |-------- node：节点操作模块，此目录下存放所有此模块的命令
>
> ​ |-------- account：钱包管理模块，此目录下存放所有此模块的命令
>
> ​ |-------- tx：交易管理模块，此目录下存放所有此模块的命令
>
> ​ |-------- staking：质押模块，此目录下存放所有此模块的命令
>
> ​ |-------- delegate：委托模块，此目录下存放所有此模块的命令
>
> ​ |-------- government：治理模块，此目录下存放所有此模块的命令
>
> ​ |-------- query：链相关的基本信息查询模块，此目录下存放所有此模块的命令
>
> ​ |-------- hedge：锁仓模块，此目录下存放所有此模块的命令

为了能让程序自动加载命令和模块，后续只需要以打补丁的方式进行发版。

工作机制：

- 加载src/.../submodule目录下的命令，保存在缓存中，用于解析输入的参数；
- 根据cli的命令和参数，解析得到对应的命令的执行方法；
- 执行命令对应的方法。

### 发版文件

发版底层二进制时，匹配当前alaya网络底层最后一个版本放到服务器上，以alaya的0.15.0.0-SNAPSHOT为例：

- maven配置pom.xml

  ```xml
   <dependency>
            <groupId>com.alaya.client</groupId>
            <artifactId>alaya-core</artifactId>
            <version>0.15.0.0-SNAPSHOT</version>
   </dependency>
  ```

- 节点配置文件node_config.json，默认存储在jar包的同目录config文件夹下

  ```json
  {"rpcAddress": "http://127.0.0.1:6789:", "hrp": "atp", "chainId": 201018}
  ```

  