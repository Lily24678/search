package com.lsy.code.search.word;

import java.util.List;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.Word;

public class Demo1 {
	public static void main(String[] args) throws Exception {
//		String text = "杨尚川是APDPlat应用级产品开发平台的作者";
		String text = "人生就是一列开往坟墓的列车，路途上会有很多站，很难有人可以自始至终陪着走完。当陪你的人要下车时，即使不舍也该心存感激，然后挥手道别。";
		List<Word> words = WordSegmenter.seg(text);		
		System.out.println(words);
		//正向最大匹配算法：针对纯英文文本的分词算法
//		List<Word> words1 = WordSegmenter.seg(text,SegmentationAlgorithm.PureEnglish);		
//		System.out.println(words1);
		//正向最大匹配算法：MaximumMatching
		List<Word> words2 = WordSegmenter.seg(text,SegmentationAlgorithm.MaximumMatching);		
		System.out.println(words2);
		//逆向最大匹配算法：ReverseMaximumMatching
		List<Word> words3 = WordSegmenter.seg(text,SegmentationAlgorithm.ReverseMaximumMatching);		
		System.out.println(words3);
		//正向最小匹配算法：MinimumMatching
		List<Word> words4 = WordSegmenter.seg(text,SegmentationAlgorithm.MinimumMatching);		
		System.out.println(words4);
		//逆向最小匹配算法：ReverseMinimumMatching
		List<Word> words5 = WordSegmenter.seg(text,SegmentationAlgorithm.ReverseMinimumMatching);		
		System.out.println(words5);
		//双向最大匹配算法：BidirectionalMaximumMatching
		List<Word> words6 = WordSegmenter.seg(text,SegmentationAlgorithm.BidirectionalMaximumMatching);		
		System.out.println(words6);
		//双向最小匹配算法：BidirectionalMinimumMatching
		List<Word> words7 = WordSegmenter.seg(text,SegmentationAlgorithm.BidirectionalMinimumMatching);		
		System.out.println(words7);
		//双向最大最小匹配算法：BidirectionalMaximumMinimumMatching
		List<Word> words8 = WordSegmenter.seg(text,SegmentationAlgorithm.BidirectionalMaximumMinimumMatching);		
		System.out.println(words8);
		//全切分算法：FullSegmentation
		List<Word> words9 = WordSegmenter.seg(text,SegmentationAlgorithm.FullSegmentation);		
		System.out.println(words9);
		//最少词数算法：MinimalWordCount
		List<Word> words10 = WordSegmenter.seg(text,SegmentationAlgorithm.MinimalWordCount);		
		System.out.println(words10);
		//最大Ngram分值算法：MaxNgramScore
		List<Word> words11 = WordSegmenter.seg(text,SegmentationAlgorithm.MaxNgramScore);		
		System.out.println(words11);
		System.out.println(text);
	}

}
