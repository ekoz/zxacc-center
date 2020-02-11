<?xml version="1.0" encoding="utf-8" standalone="yes"?>
<w:document xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas" xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math" xmlns:v="urn:schemas-microsoft-com:vml" xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing" xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing" xmlns:w10="urn:schemas-microsoft-com:office:word" xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml" xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup" xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk" xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml" xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape" mc:Ignorable="w14 wp14">
    <w:body>
        <w:p w:rsidR="00F47BF1" w:rsidRPr="00327766" w:rsidRDefault="00620CBF" w:rsidP="00327766">
            <w:pPr>
                <w:jc w:val="center" />
                <w:rPr>
                    <w:rFonts w:hint="eastAsia" />
                    <w:b />
                    <w:sz w:val="52" />
                    <w:szCs w:val="52" />
                </w:rPr>
            </w:pPr>
            <w:r w:rsidRPr="00327766">
                <w:rPr>
                    <w:rFonts w:hint="eastAsia" />
                    <w:b />
                    <w:sz w:val="52" />
                    <w:szCs w:val="52" />
                </w:rPr>
                <w:t>${name}</w:t>
            </w:r>
        </w:p>

<#list questionList as map>
    <w:p w:rsidR="00620CBF" w:rsidRPr="00327766" w:rsidRDefault="00620CBF" w:rsidP="00A852B1">
        <w:pPr>
            <w:spacing w:beforeLines="100" w:before="312" />
            <w:rPr>
                <w:rFonts w:hint="eastAsia" />
                <w:b />
                <w:sz w:val="24" />
                <w:szCs w:val="24" />
            </w:rPr>
        </w:pPr>
        <w:r w:rsidRPr="00327766">
            <w:rPr>
                <w:rFonts w:hint="eastAsia" />
                <w:b />
                <w:sz w:val="24" />
                <w:szCs w:val="24" />
            </w:rPr>
            <w:t>${map_index+1}、${map.value.name} (${map.value.score} 分)</w:t>
        </w:r>
    </w:p>

    <#if map.value.type==2>
        <w:p w:rsidR="00620CBF" w:rsidRDefault="00620CBF">
            <w:pPr>
                <w:rPr>
                    <w:rFonts w:hint="eastAsia" />
                </w:rPr>
            </w:pPr>
            <w:r>
                <w:rPr>
                    <w:rFonts w:hint="eastAsia" />
                </w:rPr>
                <w:t>A. 正确（ ）                                     B. 错误（ ）</w:t>
            </w:r>
        </w:p>

    <#else>
        <#if map.value.answers??>
            <#list map.value.answers as answer>
                <#if answer_index==0>

<w:p w:rsidR="00620CBF" w:rsidRDefault="00620CBF">
    <w:pPr>
        <w:rPr>
            <w:rFonts w:hint="eastAsia" />
        </w:rPr>
    </w:pPr>
    <w:r>
        <w:rPr>
            <w:rFonts w:hint="eastAsia" />
        </w:rPr>
        <w:t>A. ${answer.name}</w:t>
    </w:r>
</w:p>

                <#elseif answer_index==1>

<w:p w:rsidR="00620CBF" w:rsidRDefault="00620CBF">
    <w:pPr>
        <w:rPr>
            <w:rFonts w:hint="eastAsia" />
        </w:rPr>
    </w:pPr>
    <w:r>
        <w:rPr>
            <w:rFonts w:hint="eastAsia" />
        </w:rPr>
        <w:t>B. ${answer.name}</w:t>
    </w:r>
</w:p>

                <#elseif answer_index==2>

<w:p w:rsidR="00620CBF" w:rsidRDefault="00620CBF">
    <w:pPr>
        <w:rPr>
            <w:rFonts w:hint="eastAsia" />
        </w:rPr>
    </w:pPr>
    <w:r>
        <w:rPr>
            <w:rFonts w:hint="eastAsia" />
        </w:rPr>
        <w:t>C. ${answer.name}</w:t>
    </w:r>
</w:p>

                <#elseif answer_index==3>

<w:p w:rsidR="00620CBF" w:rsidRDefault="00620CBF">
    <w:pPr>
        <w:rPr>
            <w:rFonts w:hint="eastAsia" />
        </w:rPr>
    </w:pPr>
    <w:r>
        <w:rPr>
            <w:rFonts w:hint="eastAsia" />
        </w:rPr>
        <w:t>D. ${answer.name}</w:t>
    </w:r>
</w:p>

                <#elseif answer_index==4>

<w:p w:rsidR="00620CBF" w:rsidRDefault="00620CBF">
    <w:pPr>
        <w:rPr>
            <w:rFonts w:hint="eastAsia" />
        </w:rPr>
    </w:pPr>
    <w:r>
        <w:rPr>
            <w:rFonts w:hint="eastAsia" />
        </w:rPr>
        <w:t>E. ${answer.name}</w:t>
    </w:r>
</w:p>

                <#elseif answer_index==5>

<w:p w:rsidR="00620CBF" w:rsidRDefault="00620CBF">
    <w:pPr>
        <w:rPr>
            <w:rFonts w:hint="eastAsia" />
        </w:rPr>
    </w:pPr>
    <w:r>
        <w:rPr>
            <w:rFonts w:hint="eastAsia" />
        </w:rPr>
        <w:t>E. ${answer.name}</w:t>
    </w:r>
</w:p>

                </#if>
            </#list>
        </#if>
    </#if>
</#list>

        <w:sectPr w:rsidR="00620CBF">
            <w:pgSz w:w="11906" w:h="16838" />
            <w:pgMar w:top="1440" w:right="1800" w:bottom="1440" w:left="1800" w:header="851" w:footer="992" w:gutter="0" />
            <w:cols w:space="425" />
            <w:docGrid w:type="lines" w:linePitch="312" />
        </w:sectPr>
    </w:body>
</w:document>