package com.mockrunner.test.web;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllWebTests
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite("Test for com.mockrunner.test.web");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(ActionTestModuleTest.class));
        suite.addTest(new TestSuite(MockHttpSessionTest.class));
        suite.addTest(new TestSuite(MockHttpServletRequestTest.class));
        suite.addTest(new TestSuite(MockHttpServletResponseTest.class));
        suite.addTest(new TestSuite(MockServletContextTest.class));
        suite.addTest(new TestSuite(NestedTagTest.class));
        suite.addTest(new TestSuite(TagLifecycleTest.class));
        suite.addTest(new TestSuite(ServletTestModuleTest.class));
        suite.addTest(new TestSuite(TagTestModuleTest.class));
        suite.addTest(new TestSuite(MockPageContextTest.class));
        suite.addTest(new TestSuite(WebMockObjectFactoryTest.class));
        suite.addTest(new TestSuite(MapMessageResourcesTest.class));
        suite.addTest(new TestSuite(MockModuleConfigTest.class));
        suite.addTest(new TestSuite(MockFunctionMapperTest.class));
        suite.addTest(new TestSuite(MockExpressionEvaluatorTest.class));
        suite.addTest(new TestSuite(MockActionForwardTest.class));
        suite.addTest(new TestSuite(HTMLOutputModuleTest.class));
        suite.addTest(new TestSuite(MockActionMappingTest.class));
        suite.addTest(new TestSuite(TagUtilTest.class));
        suite.addTest(new TestSuite(MockJspFragmentTest.class));
        suite.addTest(new TestSuite(MockJspWriterTest.class));
        suite.addTest(new TestSuite(MockFilterChainTest.class));
        suite.addTest(new TestSuite(WebTestModuleTest.class));
        suite.addTest(new TestSuite(ActionMockObjectFactoryTest.class));
        //$JUnit-END$
        return suite;
    }
}
