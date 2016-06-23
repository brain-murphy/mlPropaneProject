package datasets;

import org.junit.*;
import org.junit.Assert;

public class TestPropaneInstance {

    private static final double[] TEST_INPUT = {24023.0, 54904.0, 25354.0, 28636.0, 7740.0, 26803.0, 21985.0, 28036.0, 32939.0, 28270.0, 31091.0, 23191.0, 26633.0, 32574.0, 38729.0, 33988.0, 25512.0, 29709.0, 33681.0, 28582.0, 31504.0, 44973.0, 50447.0, 45841.0, 45891.0, 58448.0, 69427.0, 70210.0, 65199.0, 60996.0, 52803.0, 35747.0, 25913.0, 35096.0, 34806.0, 23009.0, 31504.0, 43869.0, 37329.0, 40166.0, 69113.0, 79657.0, 53027.0, 4196.0, 58982.0, 91807.0, 94381.0, 91319.0, 106562.0, 118214.0, 109085.0, 96173.0, 103384.0, 115313.0, 108978.0, 82281.0, 52864.0, 47616.0, 47256.0, 31587.0, 62562.0, 123863.0, 168104.0, 179102.0, 165588.0, 149420.0, 133249.0, 98139.0, 45405.0, 55457.0, 95964.0, 98331.0, 63118.0, 50275.0, 100351.0, 135425.0, 131693.0, 91893.0, 38518.0, 39199.0, 65861.0, 22196.0, 66888.0, 21888.0, 48647.0, 17138.0, 34100.0, 18892.0, 32253.0, 26353.0, 29476.0, 40036.0, 32511.0, 72805.0, 25590.0, 18398.0, 93541.0, 14949.0, 88147.0, 5852.0, 57184.0, 11025.0, 14250.0, 26476.0, 29443.0, 33385.0, 49506.0, 29738.0, 45958.0, 23489.0, 26586.0, 8430.0, 23878.0, 17500.0, 25336.0, 28128.0, 26666.0, 30638.0, 38634.0, 32899.0, 49422.0, 33800.0, 55672.0, 37696.0, 55462.0, 39997.0, 51959.0, 34720.0, 49995.0, 28040.0, 51759.0, 53152.0, 31828.0, 47792.0, 36458.0, 34045.0, 34117.0, 30032.0, 17769.0, 26548.0, 16979.0, 19316.0, 26661.0, 11555.0, 32989.0, 10903.0, 38473.0, 8211.0, 41482.0, 16168.0, 35665.0, 33820.0, 25776.0, 36305.0, 42515.0, 57557.0, 36684.0, 68976.0, 26009.0, 22340.0, 63990.0, 14333.0, 46150.0, 5579.0, 24808.0, 28484.0, 9706.0, 41039.0, 21219.0, 35241.0, 37006.0, 22020.0, 41111.0, 32123.0, 28358.0, 7991.0, 40755.0, 27780.0, 30695.0, 43656.0, 14808.0, 24205.0, 42478.0, 32466.0, 38276.0, 29572.0, 57600.0, 21137.0, 82053.0, 14022.0, 90918.0, 15493.0, 79950.0};
    private static final double TEST_OUPUT = 19.200000762939453;
    private static final double ACCEPTABLE_DIFFERENCE = .00000001;

    private Instance propaneInstance;

    public static PropaneInstance generateTestPropaneInstance() {
        return new PropaneInstance(TEST_INPUT, TEST_OUPUT);
    }

    @Before
    public void setUp() {
        propaneInstance = generateTestPropaneInstance();
    }

    @Test
    public void testGetInput() {
        for (int i = 0; i < TEST_INPUT.length; i++) {
            Assert.assertEquals("input incorrect at index " + i, TEST_INPUT[i], propaneInstance.getInput()[i], ACCEPTABLE_DIFFERENCE);
        }
    }

    @Test
    public void testGetOutput() {
        Assert.assertEquals("output", TEST_OUPUT, propaneInstance.getOutput(), ACCEPTABLE_DIFFERENCE);
    }

    @Test
    public void testGetError() {
        Assert.assertEquals("positive error incorrect", - 1, propaneInstance.getDifference(TEST_OUPUT - 1), ACCEPTABLE_DIFFERENCE);

        Assert.assertEquals("negative error incorrect", 1, propaneInstance.getDifference(TEST_OUPUT + 1), ACCEPTABLE_DIFFERENCE);

        Assert.assertEquals("should have zero error", 0, propaneInstance.getDifference(TEST_OUPUT), ACCEPTABLE_DIFFERENCE);
    }
}
