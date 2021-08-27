# platoncli-java操作文档M2

## 基本操作

**注意：命令行中钱包文件和参数文件都可指定为文件的绝对路径或者相对路径。  
如果在powershell中直接以json字符串作为参数，则需要在字符串两边添加单引号，格式参考：**
```shell
java -jar platoncli-java-jar-with-dependencies.jar tx_transfer -fast -p ../param/transfer2.json -d wallet.json -c '{\"rpcAddress\":\"https://openapi.alaya.network/rpc\",\"hrp\":\"atp\",\"chainId\":201018}'
```

### 显示版本号信息

- 命令

```shell
java -jar platoncli-java-jar-with-dependencies.jar -v
```

- 参数说明

> -v/--version/-version 显示版本信息


打印信息如下：

> Command: java -jar platoncli-java-jar-with-dependencies.jar -v  
> Result: version: 0.1.0  
> revision: b5388084cb108a91ccff19b217862d9c16f0aab6  
> timestamp: 2021-02-19 10:48:52

### 显示帮助命令

- 命令

```shell
java -jar platoncli-java-jar-with-dependencies.jar -help
```

- 参数说明

> --help/-help

**打印信息如下：**

```shell
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



## 链基本信息

### 查询块高

- 命令

```shell
java -jar platon-jcli-jar-with-dependencies.jar query_blockNumber -c config/node_config.json
```

- 参数说明

> -c/--config/-config: 节点配置文件（非必填）

### 根据块高查询区块信息

- 命令

~~~shell
java -jar platon-jcli-jar-with-dependencies.jar query_getBlockByNumber -n 100 -c config/node_config.json
~~~

- 参数说明

> -c/--config/-config: 节点配置文件（非必填）
>
> -n/--number/-number: 区块的快高

### 根据区块hash查询区块信息

- 命令

~~~shell
java -jar platon-jcli-jar-with-dependencies.jar query_getBlockByNumber -h <hash> -c config/node_config.json
~~~

- 参数说明

> -c/--config/-config: 节点配置文件（非必填）
>
> -n/--hash: 区块的hash

### 查询当前的结算周期的区块奖励

- 命令

~~~shell
java -jar platon-jcli-jar-with-dependencies.jar query_getPackageReward -c config/node_config.json
~~~

- 参数说明

> -c/--config/-config:节点配置文件（非必填）

### 查询区块打包的平均时间

- 命令

~~~shell
java -jar platon-jcli-jar-with-dependencies.jar query_getAvgPackTime -c config/node_config.json
~~~

- 参数说明

> -c/--config/-config:节点配置文件（非必填）

## 钱包管理模块

### 创建钱包

创建单个钱包

- 命令

~~~shell
java -jar platon-jcli-jar-with-dependencies.jar account_new --name <walletName>
~~~

- 参数说明

> --name,-n,钱包文件名称


创建多个钱包

- 命令

~~~shell
java -jar platon-jcli-jar-with-dependencies.jar account_new --batch <Number of wallet generated in batch>
~~~

- 参数说明

> --batch,-b,批量生成钱包个数

### 查看本地钱包

- 命令

~~~shell
java -jar platon-jcli-jar-with-dependencies.jar account_check --address <address>
~~~

- 参数说明

> --address, -address, -d, 具体查看的钱包文件名称或者address，不填写具体参数查询默认目录下全部钱包文件, 非必填

### 删除钱包

- 命令

~~~shell
java -jar platon-jcli-jar-with-dependencies.jar account_delete --address <address>
~~~

- 参数说明

> --address, -address, -d, 钱包文件或者钱包地址

### 修改钱包密码

- 命令

~~~shell
java -jar platon-jcli-jar-with-dependencies.jar account_modify --address <address>
~~~

- 参数说明

> --address, -address, -d, 通过名称.json或者地址指定修改的钱包文件

### 备份钱包

- 命令

```shell
java -jar platon-jcli-jar-with-dependencies.jar account_backups --address <address> -- type <type>
```

- 参数说明

> --type,-t: 选择备份方式，助记词或者私钥。--type mnemonic 或者--type privatekey，参数支持简写 --type m,--type p
>
> --address,-d	must 选择备份钱包，通过名称.json或者地址

### 查询钱包余额

- 命令

~~~shell
java -jar platon-jcli-jar-with-dependencies.jar account_getBalance --address <address>
~~~

- 参数说明

> --address, -address, -d, 地址或者名称.json

### 离线签名

- 命令

~~~shell
java -jar platon-jcli-jar-with-dependencies.jar account_sign --address <walletName.json> --data xxxxxxxxxxxxx
~~~

- 参数说明

> --address, -address, -d, 地址或者名称.json
>
> --data, -data, 待签名数据

### 钱包恢复

- 命令

~~~shell
java -jar platon-jcli-jar-with-dependencies.jar account_recovery --type <type of recovery>
~~~

- 参数说明

> --type, -type, -t, 选择恢复方式，助记词（mnemonic）或者私钥（privateKey）

## 锁仓模块

### 创建锁仓计划

- 命令

~~~shell
java -jar platon-jcli-jar-with-dependencies.jar hedge_createRestrictingPlan  --address <walletName.json> --param xxxxxxxxxxxxx 
~~~

- 参数说明

> --address, -address, -d, 地址或者名称.json
>
> --param, -param, -p, 交易参数，json字符串，或者json文件路径

### 获取锁仓计划

- 命令

~~~shell
java -jar platon-jcli-jar-with-dependencies.jar hedge_getRestrictingInfo  --address <walletName.json>
~~~

- 参数说明

> --address, -address, -d, 地址或者名称.json

## 帮助信息

在任意命令后追加"--help"或者"-help"即可查询该命令的帮助信息。

例如

- 命令

```shell
java -jar platon-jcli-jar-with-dependencies.jar government_vote --help
```

具体命令和描述请参见[命令清单.csv]

### 查询参数模板

在任意支持模板的命令（即支持--param参数）后追加"--template"或者"-template"即可查询该命令的模板参数。

例如

- 命令

```shell
java -jar platon-jcli-jar-with-dependencies.jar government_vote --template
```

- 结果

```shell
类型    必填性      参数名称        参数解释
String  must        verifier        声明的节点，只能是验证人/候选人，若为空则默认使用config配置的节点的id
String  must        proposalId      提案ID，所投的提案交易的hash
String  must        option          投票类型；YEAS 赞成票、NAYS 反对票、ABSTENTIONS 弃权票
```

支持参数模板的命令如下

```csv
government_vote               ,
government_submitProposal     ,
government_declareVersion     ,
government_getGovernParamValue,
government_reportDoubleSign   ,
delegate_new                  ,
delegate_unDelegate           ,
delegate_getDelegateReward    ,
hedge_createRestrictingPlan   ,
staking_create                ,
staking_increase              ,
staking_unStaking             ,
staking_update
```
