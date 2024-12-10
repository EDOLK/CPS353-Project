import java.math.BigInteger;
import java.util.List;

import interfaces.BigIntegerNumStream;

public class BigIntegerNumStreamImplementation implements BigIntegerNumStream{

    List<BigInteger> bigIntList;

    public BigIntegerNumStreamImplementation(List<BigInteger> bigIntList) {
        this.bigIntList = bigIntList;
    }
    @Override
    public List<Integer> getIntegers() {
        throw new UnsupportedOperationException("Unimplemented method 'getIntegers'");
    }

    @Override
    public void setIntegerList(List<Integer> integerList) {
        throw new UnsupportedOperationException("Unimplemented method 'setIntegerList'");
    }

    @Override
    public List<BigInteger> getBigIntegers() {
        return bigIntList;
    }

    @Override
    public void setBigIntegerList(List<BigInteger> bigIntegerList) {
        this.bigIntList = bigIntegerList;
    }
    
}
