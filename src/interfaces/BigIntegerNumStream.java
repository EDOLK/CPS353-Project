package interfaces;

import java.math.BigInteger;
import java.util.List;

public interface BigIntegerNumStream extends NumStream {
    List<BigInteger> getBigIntegers();

    void setBigIntegerList(List<BigInteger> bigIntegerList);
}
