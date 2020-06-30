# search:搜索相关
## word 分词中文分词组件
> 参考文档：https://github.com/ysc/word
1. 对文本进行分词(org.apdplat.word.WordSegmenter)
2. 对文件进行分词(org.apdplat.word.WordSegmenter)
3. 自定义用户词库(org.apdplat.word.util.WordConfTools、org.apdplat.word.dictionary.DictionaryFactory)
4. 显式指定分词算法(org.apdplat.word.segmentation.SegmentationAlgorithm)
5. 词性标注(org.apdplat.word.tagging.PartOfSpeechTagging)
什么是词性标注？
词性标注（part-of-speech tagging）,又称为词类标注或者简称标注，是指为分词结果中的每个单词标注一个正确的词性的程序，也即确定每个词是名词、动词、形容词或者其他词性的过程。
6. refine(org.apdplat.word.segmentation.WordRefiner)
7. 同义标注(org.apdplat.word.tagging.SynonymTagging)、反义标注(org.apdplat.word.tagging.AntonymTagging)、拼音标注(org.apdplat.word.tagging.PinyinTagging)
8. **自定义配置word.local.conf，默认配置word.conf**


## 遗留问题
1. word.conf 配置文件中：是否使用redis的发布订阅服务来实时检测HTTP资源变更。问题是：如何使用？
2. Ngram 语言模型 是怎么计算的？
