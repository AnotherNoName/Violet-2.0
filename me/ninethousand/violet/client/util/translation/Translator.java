/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.http.client.utils.URIBuilder
 */
package me.ninethousand.violet.client.util.translation;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import me.ninethousand.violet.client.util.translation.Language;
import me.ninethousand.violet.client.util.translation.Translation;
import org.apache.http.client.utils.URIBuilder;

public class Translator {
    public Translation translate(String text, Language target) {
        return this.translate(text, Language.AUTO, target);
    }

    /*
     * Exception decompiling
     */
    public Translation translate(String text, Language source, Language target) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private URI getURI(String url, Language source, Language target, String toTranslate) throws IOException, URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(new URI(url));
        uriBuilder.addParameter("client", "dict-chrome-ex");
        uriBuilder.addParameter("sl", source.code);
        uriBuilder.addParameter("tl", target.code);
        uriBuilder.addParameter("hl", target.code);
        uriBuilder.addParameter("q", toTranslate);
        return uriBuilder.build();
    }
}

