# platoncli-java操作文档

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

## 交易模块

### 根据交易hash查询交易信息

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar tx_getTransactionReceipt -h 0x49d06123a744b3a0db0cd9c119d60b95f711e06c7837cc47f4a46dcddf8dca10
~~~

- 参数说明

> -h/--hash/-hash:交易hash

### 根据交易hash查询交易

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar tx_getTransaction -h 0x49d06123a744b3a0db0cd9c119d60b95f711e06c7837cc47f4a46dcddf8dca10
~~~

- 参数说明

> -h/--hash/-hash:交易hash

### 发送已签名交易数据

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar tx_sendOffline -data qrcodeImage.png -address wallet.json
~~~

- 参数说明

> --data/-data：已签名的交易数据或者待签名二维码文件  
> --address/-address/-d：发送交易地址的钱包json文件

### 发送交易

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar tx_transfer -p param/tx_transfer.json -address wallet.json -fast -o
~~~

- 参数说明

> -p/--param/-param:创建交易所需要的参数   
> --address/-address/-d：发送交易地址的钱包json文件  
> -t/--template/-template:查看param所需要的参数都有哪些（非必填）  
> -o/--offline/-offline:是否生成待签名文件（离线交易，非必填）  
> -f/--fast/-fast:是否使用快速发送交易功能，开启后可以快速完成发送交易但无法取得交易详情（默认false不使用，非必填）

## 质押模块

### 质押交易

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar staking_create -p staking_create_param.json -d staking.json -gasLimit 200000 -gasPrice 10000000000
~~~

- 参数说明

> -p/--param/-param:创建交易所需要的参数  
> -d/--address/-address:钱包名或钱包地址  
> -t/--template/-template:查看param所需要的参数都有哪些（非必填）  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）  
> -o/--offline/-offline:是否生成待签名文件（离线交易，非必填）  
> -f/--fast/-fast:是否使用快速发送交易功能，开启后可以快速完成发送交易但无法取得交易详情（默认false不使用，非必填）  
> --gasLimit/-gasLimit：gas用量限制（默认值100000，非必填）  
> --gasPrice/-gasPrice：gas价格（默认值1000000000，单位von，非必填）

### 修改质押信息

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar staking_update -p param/staking_update.json -d staking.json
~~~

- 参数说明

> -p/--param/param:创建交易所需要的参数  
> -d/--address/-address:钱包名或钱包地址  
> -t/--template/-template:查看param所需要的参数都有哪些（非必填）  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）  
> -o/--offline/-offline:是否生成待签名文件（离线交易，非必填）  
> -f/--fast/-fast:是否使用快速发送交易功能，开启后可以快速完成发送交易但无法取得交易详情（默认false不使用，非必填）   
> --gasLimit/-gasLimit：gas用量限制（默认值100000，非必填）  
> --gasPrice/-gasPrice：gas价格（默认值1000000000，单位von，非必填）

### 退出质押交易

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar staking_unStaking -d staking.json -p param/staking_unStaking.json
~~~

- 参数说明

> -p/--param/param:创建交易所需要的参数  
> -d/--address/-address:钱包名或钱包地址  
> -t/--template/-template:查看param所需要的参数都有哪些（非必填）  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）  
> -o/--offline/-offline:是否生成待签名文件（离线交易，非必填）  
> -f/--fast/-fast:是否使用快速发送交易功能，开启后可以快速完成发送交易但无法取得交易详情（默认false不使用，非必填）  
> --gasLimit/-gasLimit：gas用量限制（默认值100000，非必填）  
> --gasPrice/-gasPrice：gas价格（默认值1000000000，单位von，非必填）

### 增持质押交易

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar staking_increase -d staking.json -p param/staking_increase.json
~~~

- 参数说明

> -p/--param/param:创建交易所需要的参数  
> -d/--address/-address:钱包名或钱包地址  
> -t/--template/-template:查看param所需要的参数都有哪些（非必填）  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）  
> -o/--offline/-offline:是否生成待签名文件（离线交易，非必填）  
> -f/--fast/-fast:是否使用快速发送交易功能，开启后可以快速完成发送交易但无法取得交易详情（默认false不使用，非必填）  
> --gasLimit/-gasLimit：gas用量限制（默认值100000，非必填）  
> --gasPrice/-gasPrice：gas价格（默认值1000000000，单位von，非必填）

### 查询

- 命令

依次对应查询当前结算周期的验证人列表、查询当前共识周期的验证人列表、查询所有实时候选人列表、根据nodeId查询节点质押信息、查询当前的结算周期的质押奖励

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar staking_getVerifierList			
java -jar platoncli-java-jar-with-dependencies.jar staking_getValidatorList			
java -jar platoncli-java-jar-with-dependencies.jar staking_getCandidateList			
java -jar platoncli-java-jar-with-dependencies.jar staking_getCandidateInfo  -nodeId <nodeId>		
java -jar platoncli-java-jar-with-dependencies.jar staking_getStakingReward
~~~

- 参数说明

> --nodeId/-nodeId:节点id，若为空，则表示查询所有的节点，非必填

## 委托模块

### 创建委托

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar delegate_new -p param/delegate_new.json -d wallet.json
~~~

- 参数说明

> -p/--param/param:创建交易所需要的参数  
> -d/--address/-address:钱包名或钱包地址  
> -t/--template/-template:查看param所需要的参数都有哪些（非必填）  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）  
> -o/--offline/-offline:是否生成待签名文件（离线交易，非必填）  
> -f/--fast/-fast:是否使用快速发送交易功能，开启后可以快速完成发送交易但无法取得交易详情（默认false不使用，非必填）  
> --gasLimit/-gasLimit：gas用量限制（默认值100000，非必填）  
> --gasPrice/-gasPrice：gas价格（默认值1000000000，单位von，非必填）

### 减持/撤销委托

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar delegate_unDelegate -p param/delegate_unDelegate.json -d wallet.json
~~~

- 参数说明

> -p/--param/param:创建交易所需要的参数  
> -d/--address/-address:钱包名或钱包地址  
> -t/--template/-template:查看param所需要的参数都有哪些（非必填）  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）  
> -o/--offline/-offline:是否生成待签名文件（离线交易，非必填）  
> -f/--fast/-fast:是否使用快速发送交易功能，开启后可以快速完成发送交易但无法取得交易详情（默认false不使用，非必填）  
> --gasLimit/-gasLimit：gas用量限制（默认值100000，非必填）  
> --gasPrice/-gasPrice：gas价格（默认值1000000000，单位von，非必填）

### 查询账户在各节点未提取委托奖励

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar delegate_getDelegateReward -p param.json
~~~

- 参数说明

> --help/-help:查询帮助  
> -p/--param/param:创建交易所需要的参数  
> -d/--address/-address:钱包名或钱包地址  
> -t/--template/-template:查看param所需要的参数都有哪些（非必填）  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）

### 提取委托奖励

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar delegate_withdrawDelegateReward -d wallet/wallet1.json
~~~

- 参数说明

> --help/-help:查询帮助  
> -d/--address/-address:钱包名或钱包地址  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）  
> -o/--offline/-offline:是否生成待签名文件（离线交易，非必填）  
> -f/--fast/-fast:是否使用快速发送交易功能，开启后可以快速完成发送交易但无法取得交易详情（默认false不使用，非必填）  
> --gasLimit/-gasLimit：gas用量限制（默认值100000，非必填）  
> --gasPrice/-gasPrice：gas价格（默认值1000000000，单位von，非必填）

### 查询当前账户地址所委托的节点的NodeId和质押Id

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar delegate_getRelatedListByDelAddress -d wallet.json
~~~

- 参数说明

> --help/-help:查询帮助  
> -d/--address/-address:钱包名或钱包地址  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）

## 治理模块

### 提案相关

- 文本提案参数（写入到government.json文件）

~~~
{
  "verifier": "da99eb65da965e24684be1703a25e434a8a2036b19def8b4563cc16a8463b76abf44ef5bf639d790e4ce3a8fcb6697d1fd7e9140ad61438ebb492fba5dd931a2",
  "pIDID": "012345678",
  "transaction_cfg":{"gas":1000000,
                   "gasPrice":3000000000000000,
                   "nonce":null}
}
~~~

- 升级提案（写入到government.json文件）

~~~
{
  "verifier": "da99eb65da965e24684be1703a25e434a8a2036b19def8b4563cc16a8463b76abf44ef5bf639d790e4ce3a8fcb6697d1fd7e9140ad61438ebb492fba5dd931a2",
  "pIDID": "202101041633",
  "endVotingRound": 1,
  "newVersion": 202111,
  "transaction_cfg":{"gas":1000000,
                   "gasPrice":3000000000000000,
                   "nonce":null}
}
~~~

- 参数提案（写入到government.json文件）

~~~
{
  "verifier": "da99eb65da965e24684be1703a25e434a8a2036b19def8b4563cc16a8463b76abf44ef5bf639d790e4ce3a8fcb6697d1fd7e9140ad61438ebb492fba5dd931a2",
  "pIDID": "202101041643",
  "module": "reward",
  "name": "increaseIssuanceRatio",
  "newValue": "700",
  "transaction_cfg": {
    "gas": 1000000,
    "gasPrice": 3000000000000000,
    "nonce": null
  }
}
~~~

- 删除提案（写入到government.json文件）

~~~
{
  "verifier": "da99eb65da965e24684be1703a25e434a8a2036b19def8b4563cc16a8463b76abf44ef5bf639d790e4ce3a8fcb6697d1fd7e9140ad61438ebb492fba5dd931a2",
  "pIDID": "202101041633",
  "endVotingRound": 1,
  "canceledProposalID": "e76954762f11994cd3cb619149f0995a907c09542e8c283aaed3faccc87b8383",
  "transaction_cfg":{"gas":1000000,
                   "gasPrice":3000000000000000,
                   "nonce":null}
}
~~~

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar government_submitProposal -p param/government.json -d wallet.json -m TextProposal -o
~~~

- 参数说明

> -m/--module/-module:提案类型：文本（TextProposal）、升级（VersionProposal）、参数（ParamProposal）、取消提案（CancelProposal）
> --help/-help:查询帮助  
> -d/--address/-address:钱包名或钱包地址  
> -t/--template/-template:查看param所需要的参数都有哪些（非必填）  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）  
> -o/--offline/-offline:是否生成待签名文件（离线交易，非必填）  
> -f/--fast/-fast:是否使用快速发送交易功能，开启后可以快速完成发送交易但无法取得交易详情（默认false不使用，非必填）

### 查询提案列表

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar government_listProposal
~~~

- 参数说明

> --help/-help:查询帮助  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）

### 查询治理参数列表

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar government_listGovernParam -m staking
~~~

- 参数说明

> --help/-help:查询帮助  
> -m/--module/-module:查询具体的参数模块，输入""空串，表示查询所有治理参数。包含staking、slashing、block、reward、restricting等模块  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）

### 根据提案id查询提案信息

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar government_getProposal -pid xxxxxx
~~~

- 参数说明

> --help/-help:查询帮助  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）  
> --pid/-pid:提案id

### 查询最新的治理参数值

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar government_getGovernParamValue -p param/government_getGovernParamValue.json
~~~

- 参数说明

> --help/-help:查询帮助  
> -p/--param/param:创建交易所需要的参数   
> -t/--template/-template:查看param所需要的参数都有哪些（非必填）  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）

### 查询节点的链生效版本

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar government_getActiveVersion
~~~

- 参数说明

> --help/-help:查询帮助  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）

### 查询提案结果

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar government_getTallyResult -pid xxxx
~~~

- 参数说明

> --help/-help:查询帮助  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）  
> --pid/-pid:提案id

### 查询提案的累计可投票人数

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar government_getAccuVerifiersCount -h xxxx -pid xxxx
~~~

- 参数说明

> --help/-help:查询帮助  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）  
> --pid/-pid:提案id  
> -h/--hash/-hash:当前最新区块的hash

### 查询节点是否已被举报过多签

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar government_checkDoubleSign -t 2 -n xxx -b 10000
~~~

- 参数说明

> -t/--type/-type：多签类型，代表双签类型，1：prepareBlock，2：prepareVote，3：viewChange  
> --nodeId/-nodeId:节点id  
> --number/-number/-n:块高  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）

### 提案投票

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar government_vote -p param/government_vote.json -d wallet.json
~~~

- 参数说明

> -p/--param/-param:创建交易所需要的参数  
> -d/--address/-address:钱包名或钱包地址  
> -t/--template/-template:查看param所需要的参数都有哪些（非必填）  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）  
> -o/--offline/-offline:是否生成待签名文件（离线交易，非必填）  
> -f/--fast/-fast:是否使用快速发送交易功能，开启后可以快速完成发送交易但无法取得交易详情（默认false不使用，非必填）  
> --gasLimit/-gasLimit：gas用量限制（默认值100000，非必填）  
> --gasPrice/-gasPrice：gas价格（默认值1000000000，单位von，非必填）

### 版本声明

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar government_declareVersion -p param/government_declareVersion.json -d wallet.json
~~~

- 参数说明

> -p/--param/-param:创建交易所需要的参数  
> -d/--address/-address:钱包名或钱包地址  
> -t/--template/-template:查看param所需要的参数都有哪些（非必填）  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）  
> -o/--offline/-offline:是否生成待签名文件（离线交易，非必填）  
> -f/--fast/-fast:是否使用快速发送交易功能，开启后可以快速完成发送交易但无法取得交易详情（默认false不使用，非必填）  
> --gasLimit/-gasLimit：gas用量限制（默认值100000，非必填）  
> --gasPrice/-gasPrice：gas价格（默认值1000000000，单位von，非必填）
>

### 举报双签

- 命令

~~~shell
java -jar platoncli-java-jar-with-dependencies.jar government_reportDoubleSign -d wallet.json -p param/government_reportDoubleSign.json
~~~

- 参数说明

> -p/--param/-param:创建交易所需要的参数  
> -d/--address/-address:钱包名或钱包地址  
> -t/--template/-template:查看param所需要的参数都有哪些（非必填）  
> -c/--config/-config:节点配置文件（默认jar同目录下的config文件夹里的node_config.json，非必填）  
> -o/--offline/-offline:是否生成待签名文件（离线交易，非必填）  
> -f/--fast/-fast:是否使用快速发送交易功能，开启后可以快速完成发送交易但无法取得交易详情（默认false不使用，非必填）  
> --gasLimit/-gasLimit：gas用量限制（默认值100000，非必填）  
> --gasPrice/-gasPrice：gas价格（默认值1000000000，单位von，非必填）



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
