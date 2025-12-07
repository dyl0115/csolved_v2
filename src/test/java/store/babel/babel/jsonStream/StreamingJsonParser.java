package store.babel.babel.jsonStream;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StreamingJsonParser
{
    private enum State
    {
        OUTSIDE,           // 문자열 바깥
        IN_KEY,            // 키 문자열 안
        AFTER_KEY,         // 키 끝나고 : 기다리는 중
        BEFORE_VALUE,      // : 이후 값 시작 전
        IN_STRING_VALUE,   // 문자열 값 안
        IN_OTHER_VALUE,    // 숫자, boolean 등
        IN_ARRAY,          // 배열 안
        IN_ARRAY_STRING    // 배열 내 문자열 안
    }

    private State state = State.OUTSIDE;
    private StringBuilder currentKey = new StringBuilder();
    private StringBuilder currentValue = new StringBuilder();
    private boolean escaped = false;
    private int arrayDepth = 0;
    private int objectDepth = 0;

    /**
     * 청크를 처리하고, 이 청크에서 파싱된 필드:값 쌍을 반환
     * 값이 스트리밍 중인 경우 부분 값도 포함됨
     */
    public Map<String, String> processStreamingJson(String chunk)
    {
        Map<String, String> result = new LinkedHashMap<>();

        for (char c : chunk.toCharArray())
        {
            processChar(c, result);
        }

        return result;
    }

    private void processChar(char c, Map<String, String> result)
    {
        // 이스케이프 처리
        if (escaped)
        {
            if (state == State.IN_STRING_VALUE)
            {
                handleEscapedChar(c);
                result.put(currentKey.toString(), currentValue.toString());
                currentValue.setLength(0);
            }
            else if (state == State.IN_ARRAY_STRING)
            {
                handleEscapedChar(c);
                result.put(currentKey.toString() + "[]", currentValue.toString());
                currentValue.setLength(0);
            }
            else if (state == State.IN_KEY)
            {
                currentKey.append(c);
            }
            escaped = false;
            return;
        }

        // 백슬래시 처리
        if (c == '\\')
        {
            escaped = true;
            return;
        }

        switch (state)
        {
            case OUTSIDE:
                if (c == '"')
                {
                    state = State.IN_KEY;
                    currentKey.setLength(0);
                }
                else if (c == '{')
                {
                    objectDepth++;
                }
                else if (c == '}')
                {
                    objectDepth--;
                }
                break;

            case IN_KEY:
                if (c == '"')
                {
                    state = State.AFTER_KEY;
                }
                else
                {
                    currentKey.append(c);
                }
                break;

            case AFTER_KEY:
                if (c == ':')
                {
                    state = State.BEFORE_VALUE;
                }
                break;

            case BEFORE_VALUE:
                if (c == '"')
                {
                    state = State.IN_STRING_VALUE;
                    currentValue.setLength(0);
                }
                else if (c == '[')
                {
                    state = State.IN_ARRAY;
                    arrayDepth = 1;
                }
                else if (c == '{')
                {
                    // 중첩 객체 - 단순화를 위해 스킵
                    state = State.OUTSIDE;
                    objectDepth++;
                }
                else if (!Character.isWhitespace(c))
                {
                    // 숫자, boolean, null 등
                    state = State.IN_OTHER_VALUE;
                    currentValue.setLength(0);
                    currentValue.append(c);
                }
                break;

            case IN_STRING_VALUE:
                if (c == '"')
                {
                    state = State.OUTSIDE;
                }
                else
                {
                    currentValue.append(c);
                    result.put(currentKey.toString(), currentValue.toString());
                    currentValue.setLength(0);
                }
                break;

            case IN_OTHER_VALUE:
                if (c == ',' || c == '}')
                {
                    result.put(currentKey.toString(), currentValue.toString().trim());
                    currentValue.setLength(0);
                    state = State.OUTSIDE;
                    if (c == '}') objectDepth--;
                }
                else
                {
                    currentValue.append(c);
                }
                break;

            case IN_ARRAY:
                if (c == '"')
                {
                    state = State.IN_ARRAY_STRING;
                    currentValue.setLength(0);
                }
                else if (c == ']')
                {
                    arrayDepth--;
                    if (arrayDepth == 0)
                    {
                        state = State.OUTSIDE;
                    }
                }
                else if (c == '[')
                {
                    arrayDepth++;
                }
                break;

            case IN_ARRAY_STRING:
                if (c == '"')
                {
                    state = State.IN_ARRAY;
                }
                else
                {
                    currentValue.append(c);
                    result.put(currentKey.toString() + "[]", currentValue.toString());
                    currentValue.setLength(0);
                }
                break;
        }
    }

    private void handleEscapedChar(char c)
    {
        switch (c)
        {
            case 'n':
                currentValue.append('\n');
                break;
            case 't':
                currentValue.append('\t');
                break;
            case 'r':
                currentValue.append('\r');
                break;
            case '"':
                currentValue.append('"');
                break;
            case '\\':
                currentValue.append('\\');
                break;
            default:
                currentValue.append(c);
        }
    }

    // 파서 상태 리셋
    public void reset()
    {
        state = State.OUTSIDE;
        currentKey.setLength(0);
        currentValue.setLength(0);
        escaped = false;
        arrayDepth = 0;
        objectDepth = 0;
    }
}
