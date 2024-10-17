package rainbow_tables.utils.reduction;

import java.math.BigInteger;

import rainbow_tables.utils.wordgen.IWordGenerator;
import rainbow_tables.utils.wordgen.MotGenerator;

/**
 * Reduction method taking a seed and creating a random clear password using
 * that seed.
 * Accepts an offset on the seed, allowing a near infinite "types" of reduction
 * method,
 * with an offset increasing by 1 each reduction made on a rainbow table.
 */
public class SeededReduction extends AReduction {

    private int offset;
    private IWordGenerator wordGenerator;

    public SeededReduction(int offset, IWordGenerator wordGenerator) {
        this.offset = offset;
        this.wordGenerator=wordGenerator;
    }

    @Override
    public String reduce(String s, int n) {
        BigInteger seed = new BigInteger(s, 16);
        int intSeed = seed.intValue();
        return wordGenerator.motAleatoire(n, intSeed + offset);
    }

}
