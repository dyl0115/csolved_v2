package store.babel.babel.jsonStream;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.async.ByteArrayFeeder;
import com.fasterxml.jackson.core.async.NonBlockingInputFeeder;
import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.tokens.BlockEndToken;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

public class JsonStreamTest
{
    @Test
    public void processStreamJsonWithPartialText() throws IOException
    {

    }


    @Getter
    @Builder
    static class SamplePost
    {
        private String title;
        private String content;
        private List<String> tags;

        public static SamplePost create()
        {
            return SamplePost.builder()
                    .title("무인카페를 고르는 팁")
                    .content("새로운 게시글의 제목입니다. 안녕하세요.\n" +
                            "저는 카페에서 커피를 먹으면서 코드를 작성하고 있어요.\n" +
                            "저는 중국, 일본 등 동아시아의 문화에 대해서 관심이 많아요.\n" +
                            "지금은 12월 인데요. 무인 카페를 이용할 때 가장 중요한 것은 첫째로\n" +
                            "난방이 잘되는가. 둘째로 사람이 많이 없는가. 음악소리가 너무 크지는 않은가\n" +
                            "셋째로 커피의 가격이 저렴한가 입니다. \n" +
                            "아, 그리고 정말 중요한 것은 내가 앉아서 작업하는 의자가 편한가? 이것도 매우 중요합니다.\n" +
                            "카페에 오래 앉아서 작업을 하다보면 허리가 아픈 경우가 너무 많거든요. \n" +
                            "모두 허리 건강 잘 관리하시면서 행복한 하루 되세요. 고마워요.")
                    .tags(List.of("무인카페", "맛있는 커피", "겨울"))
                    .build();
        }

        public static List<String> createJsonStream() throws JsonProcessingException
        {
            ObjectMapper mapper = new ObjectMapper();
            SamplePost post = SamplePost.create();
            String json = mapper.writeValueAsString(post);

            List<String> chunks = new ArrayList<>();
            Random random = new Random();

            int index = 0;
            int length = json.length();

            while (index < length)
            {
                int remaining = length - index;

                // 최소 1 글자, 최대 5 글자 랜덤 청크
                int chunkSize = 1 + random.nextInt(Math.min(5, remaining));

                int end = Math.min(index + chunkSize, length);
                chunks.add(json.substring(index, end));

                index = end;
            }

            return chunks;
        }
    }
}
