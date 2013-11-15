package pwgen;

import java.util.Random;

/**
 * Generates random secure passwords.
 */
public class PasswordGenerator {

    private int count;
    private int length;  //not used yet
    private final char[] lowers = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private final char[] uppers = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final char[] numbers = "0123456789".toCharArray();
    private final char[] spec = "+!%/=().,".toCharArray();

    /**
     * Contructor for generating only 1, 8 characters long password.
     */
    public PasswordGenerator() {
        this(1, 8);
    }

    /**
     * Constructor for generating <code>count</code> number of 
     * 8 characters long passwords.
     * @param count The number of passwords
     */
    public PasswordGenerator(int count) {
        this(count, 8);
    }
    
    
    /**
     * Constructor for generating <code>count</code> number of 
     * <code>length</code> characters long passwords.
     * @param count The number of passwords
     * @param length The length of each password
     */
    public PasswordGenerator(int count, int length){
        this.count = count;
        this.length = length;
        //TODO Check the values of these.
    }

    public String[] getPasswords() {
        String[] buffer = new String[count];
        
        for (int i = 0; i < count; i++) {
            buffer[i] = getOne(length);
        }
        return buffer;
    }

    private String getOne(int chars) {
        StringBuilder builder = new StringBuilder(chars);
        for (int i = 0; i < chars; i++) {
            builder.append(getChar());
        }
        return builder.toString();
    }
    
    private char getChar(){
        Random r = new Random();
        int whichChar;
        int whichSet = r.nextInt(4);
        switch(whichSet){
            case 0:
                whichChar = r.nextInt(lowers.length);
                return lowers[whichChar];
            case 1:
                whichChar = r.nextInt(uppers.length);
                return uppers[whichChar];
            case 2:
                whichChar = r.nextInt(numbers.length);
                return numbers[whichChar];
            case 3:
                whichChar = r.nextInt(spec.length);
                return spec[whichChar];
            default:
                return '0';
        }
    }
}
