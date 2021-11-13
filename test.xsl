<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"><xsl:output indent="yes"/>
    <xsl:template match="/tests">

        <suite name="test relis">
            <test verbose="2" preserve-order="true" name="data management test">
                <classes>
                    <xsl:for-each select="test">
                        <xsl:if test="@name = 'management'">

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
                        </xsl:if>
                    </xsl:for-each>

                    <class name="ProjectTest">
                        <methods>
                        <xsl:for-each select="test">
                        <!--  SCREENING TEST -->
                        <xsl:if test="@name = 'Screening' or @name='qa'">
                                <xsl:for-each select="case">
                                    <include>
                                        <xsl:attribute name="name">
                                            <xsl:value-of select="@code" />
                                        </xsl:attribute>
                                    </include>
                                </xsl:for-each>
                        </xsl:if>
                        </xsl:for-each>
                        </methods>
                    </class>
                </classes>
            </test>
        </suite>
    </xsl:template>


</xsl:transform>

