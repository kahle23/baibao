package baibao.ai.llm.support.openai;

import artoria.ai.AiUtils;
import artoria.data.Dict;
import artoria.data.json.JsonUtils;
import artoria.data.json.support.FastJsonHandler;
import com.alibaba.fastjson.JSON;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Ignore
public class OpenAiEngineTest {
    private static final Logger log = LoggerFactory.getLogger(OpenAiEngineTest.class);
    private static final String engineName = "openai";

    static {
        JsonUtils.registerHandler("default", new FastJsonHandler());
        OpenAiEngine aiEngine = new OpenAiEngine("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");
        aiEngine.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 58591)));
        AiUtils.registerEngine(engineName, aiEngine);
    }

    @Test
    public void test1() {
        String strategy = "models";
        Dict execute = AiUtils.execute(engineName, (Object) null, strategy, Dict.class);
        log.info(JSON.toJSONString(execute, true));
    }

    @Test
    public void test2() {
        String strategy = "completion";
        String execute = AiUtils.execute(engineName, "what is ai?", strategy, String.class);
        log.info(execute);
//        Dict execute = AiUtils.execute("what is ai?", engineName, strategy, Dict.class);
//        log.info(JSON.toJSONString(execute, Boolean.TRUE));
    }

    @Test
    public void test3() {
        String strategy = "chat";
        String content = "目前有以下功能处理器：\n" +
                "名称：邮件发送处理器\n" +
                "关键词：邮件发送\n" +
                "\n" +
                "名称：知识库数据查询处理器\n" +
                "关键词：知识库、员工守则、公司规定、公司新规、工龄、工龄计算规则、请假\n" +
                "\n" +
                "名称：百度数据查询处理器\n" +
                "关键词：互联网数据查询、百度查询、百度数据\n" +
                "\n" +
                "名称：员工信息查询处理器\n" +
                "关键词：员工信息、员工、员工数据、入离职信息\n" +
                "\n" +
                "请根据用户问题，判断需要调用哪几个功能处理器，用英文逗号分隔，需要优先执行的在前，一般情况下数据查询类处理器需要优先执行。\n" +
                "不要写任何解释或其他文字和标点符号。\n" +
                "\n" +
                "用户问题：给张三发送离职信息邮件，邮件标题为张三的毕业祝贺，邮件内容为张三的基本信息的内容、张三的工龄（工龄需要根据公司的规定计算得出），并在邮件尾部加上祝您找到的更好的工作。";
        String execute = AiUtils.execute(engineName, content, strategy, String.class);
        log.info(execute);
    }

}
