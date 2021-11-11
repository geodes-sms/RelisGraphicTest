<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"><xsl:output indent="yes"/>
    <xsl:template match="/tests">

        <suite name="test relis">
            <test verbose="2" preserve-order="true" name="data management test">
                    <xsl:for-each select="test">
                        <xsl:if test="@name = 'management'">
                            <classes>
                            <class name="DataManagerTest">
                                  <methods>
                               <xsl:for-each select="case">
                                <include>
                                    <xsl:attribute name="name">
                                        <xsl:value-of select="@code" />
                                    </xsl:attribute>
                                </include>
                               </xsl:for-each>
                                  </methods>

                            </class>
                            </classes>
                        </xsl:if>

                    </xsl:for-each>

            </test>
        </suite>
    </xsl:template>


</xsl:transform>

