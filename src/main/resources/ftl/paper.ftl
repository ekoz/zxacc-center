${name}

<#list questionList as map>

${map_index+1}、${map.value.name} (${map.value.score} 分)（${map.value.finalTof}）
    <#if map.value.type==2>
A. 正确（ ）                                     B. 错误（ ）
    <#else>
        <#if map.value.answers??>
            <#list map.value.answers as answer>
                <#if answer_index==0>
A. ${answer.name}
                <#elseif answer_index==1>
B. ${answer.name}
                <#elseif answer_index==2>
C. ${answer.name}
                <#elseif answer_index==3>
D. ${answer.name}
                <#elseif answer_index==4>
E. ${answer.name}
                <#elseif answer_index==5>
F. ${answer.name}
                </#if>
            </#list>
        </#if>
    </#if>
</#list>
