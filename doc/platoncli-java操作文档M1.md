# platoncli-java操作文档M1

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
