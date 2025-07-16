package com.stanzaliving.laundry.util;

import com.stanzaliving.secure.constants.StanzaConstants;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

@UtilityClass
public class StanzaUtils {

    private static RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    private static int SCALE = 3;

    public static String hideSecret(String secretString) {

        if (StringUtils.isNotBlank(secretString)) {
            return "******";
        }

        return StringUtils.EMPTY;
    }

    public double roundOffToNine(double number) {
        double lastTwoDigits = number % 100;

        if (lastTwoDigits > 0 && lastTwoDigits < 49) {
            number = number - lastTwoDigits + 49;
        } else {
            number = number - lastTwoDigits + 99;
        }
        return number;
    }

    public static String convertIntegerNumberToString(int number) {
        return " [" + number + "] ";
    }

    public static String convertDoublePriceToString(double price) {
        return " [" + price + "] ";
    }

    public static String getOccupancyString(int occupancy) {

        switch (occupancy) {
            case 1:
                return "[Single]";
            case 2:
                return "[Double]";
            case 3:
                return "[Triple]";
            case 4:
                return "[Four]";
            case 5:
                return "[Five]";
            default:
                return "[" + occupancy + "]";
        }

    }

    public static int generateOTP() {
        return ThreadLocalRandom.current().nextInt(100000, 1000000);
    }

    public static int generateOTPOfLength(int length) {
        return (int) ThreadLocalRandom.current().nextDouble(Math.pow(10, length - 1), Math.pow(10, length));
    }

    public static boolean checkIfNumber(String str) {
        return StringUtils.isNotBlank(str) && str.matches("[0-9]+");
    }

    public static int generateDefaultOtpOfLength(int length) {

        int otp = 0;
        int count = 4;

        for (int i = 0; i < length; i++) {

            otp = (otp * 10) + count;

            count++;
        }

        return otp;
    }

    /**
     * Return a random integer number between the given range inclusive @start and
     * excluding @end
     *
     * @param start - the smallest random number required
     * @param end   - one less than the maximum random number required
     * @return random integer number between the given range inclusive @start and
     * excluding @end
     * @throws IllegalArgumentException - if start is greater than equal to end
     */
    public static int getRandomNumberBetweenRange(int start, int end) {
        return ThreadLocalRandom.current().nextInt(start, end);
    }


    public static String generateUniqueIdInLowerCase(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toLowerCase();
    }

    public static String generateUniqueId(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toUpperCase();
    }

    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    public static String generateUniqueEmailId() {
        return generateUniqueId().replaceAll("-", "") + StanzaConstants.ORGANIZATION_EMAIL_DOMAIN;
    }

    public static float roundToPlaces(float value, int places) {
        float numberToDivide = 1;

        while (places > 0) {
            numberToDivide *= 10;
            places--;
        }

        Float number = Float.valueOf(numberToDivide);

        return Math.round(value * number) / number;
    }

    public static double roundToPlaces(double value, int places) {
        double numberToDivide = 1;

        while (places > 0) {
            numberToDivide *= 10;
            places--;
        }

        Double number = Double.valueOf(numberToDivide);
        return Math.round(value * number) / number;
    }

    public static String getRoundedValueString(BigDecimal value, int places) {
        value.setScale(places, RoundingMode.HALF_EVEN);
        return value.toPlainString();
    }

    public static BigDecimal getRoundedValue(BigDecimal value, int places) {
        return value.setScale(places, RoundingMode.HALF_EVEN);
    }


    public static double roundOff(Double price) {

        if (price != null) {
            return roundToPlaces(price, StanzaConstants.PRICE_ROUND_OFF_DIGITS);
        }

        return 0d;
    }

    public static List<String> getSplittedListOnComma(String input) {

        if (Objects.isNull(input)) {
            return null;
        }

        return new ArrayList<>(Arrays.asList(input.split("\\s*,\\s*")));
    }

    /***
     * Returns the percentage value of the supplied number as {@link BigDecimal}.
     *
     * @param number {@link Number}
     * @return {@link BigDecimal}
     * @author debendra.dhinda
     */
    public static BigDecimal getPercentageValueOf(Number number) {
        return getBigDecimalValueOf(number).divide(getBigDecimalValueOf(100), StanzaUtils.SCALE,
                StanzaUtils.ROUNDING_MODE);
    }

    /***
     * Returns the percentage value of the supplied string as {@link BigDecimal}.
     *
     * @param number {@link String}
     * @return {@link BigDecimal}
     * @author debendra.dhinda
     */
    public static BigDecimal getPercentageValueOf(String number) {
        return getBigDecimalValueOf(number).divide(getBigDecimalValueOf(100), StanzaUtils.SCALE,
                StanzaUtils.ROUNDING_MODE);
    }


    public static BigDecimal getBigDecimalValueOf(Number number) {
        // return new BigDecimal(Double.toString(value));
        return new BigDecimal(number.toString(), MathContext.DECIMAL128);
    }

    public static BigDecimal getBigDecimalValueOf(String value) {
        // return new BigDecimal(Double.toString(value));
        return new BigDecimal(value, MathContext.DECIMAL128);
    }

    /**
     * Used {@link Math} class's {@code ceil()} method for rounding the supplied
     * {@link BigDecimal} value.
     *
     * @param value {@link BigDecimal} value to be ceiled
     * @return value
     * @author debendra.dhinda
     */
    public static Double roundOff(BigDecimal value) {
        // return (value!=null)? value.setScale(1,
        // BigDecimal.ROUND_HALF_UP).doubleValue() :0;
        return (value != null) ? Math.ceil(value.doubleValue()) : 0;
    }

    public static double findPercentage(long total, long number) {
        return (total != 0) ? Math.round((number * 100.0 / total) * 100.0) / 100.0 : 0;
    }

    public static String formatToIndianNumberFormat(long value) {
        if (value <= 999)
            return Long.toString(value);
        String thousandsPart = (value + "").substring((value + "").length() - 3);

        long rest = value / 1000;
        NumberFormat format = new DecimalFormat("##,##");
        String formattedString = format.format(rest);
        return formattedString + "," + thousandsPart;
    }

    public static String formatBigDecimalToIndianNumberFormat(BigDecimal value) {
        Locale locale = new Locale("en", "IN");
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);
        return decimalFormat.format(value);
    }

    public static String formatBigDecimalToIndianNumberFormatWithFractions(BigDecimal value, int numFractions) {
        Locale locale = new Locale("en", "IN");
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);
        decimalFormat.setMaximumFractionDigits(numFractions);
        decimalFormat.setMinimumFractionDigits(numFractions);
        return decimalFormat.format(value);
    }

    public static String formatBigDecimalToIndianNumberFormatWithTwoDecimals(BigDecimal value) {
        Locale locale = new Locale("en", "IN");
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getInstance(locale);
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        return decimalFormat.format(value);
    }


    /**
     * Convert the supplied size to it's corresponding unit such as bytes,KB,MB,GB
     * or TB.
     *
     * @param size size to convert.
     * @author debendra.dhinda
     */
    public static String convertSizeToBytesOrKBOrMBOrGb(long size) {
        String sizeWithUnit = "";

        double kb = size / 1024;
        double mb = kb / 1024;
        double gb = mb / 1024;
        double tb = gb / 1024;

        if (size < 1024L) {
            sizeWithUnit = size + " Bytes";
        } else if (size < (1024L * 1024)) {
            sizeWithUnit = String.format("%.2f", kb) + " KB";
        } else if (size < (1024L * 1024 * 1024)) {
            sizeWithUnit = String.format("%.2f", mb) + " MB";
        } else if (size < (1024L * 1024 * 1024 * 1024)) {
            sizeWithUnit = String.format("%.2f", gb) + " GB";
        } else {
            sizeWithUnit = String.format("%.2f", tb) + " TB";
        }

        return sizeWithUnit;
    }

    public String getQuotedString(Collection<String> list) {
        return "'" + String.join("','", list) + "'";
    }

    public String getString(Object obj) {
        return Objects.isNull(obj) ? null : obj.toString();
    }

    public double gstRoundOff(double amount, double gstPercentage) {
        return gstRoundOff(amount * (gstPercentage / 100));
    }

    public double gstRoundOff(double gstAmount) {
        BigDecimal amount = BigDecimal.valueOf(gstAmount);
        amount = amount.setScale(0, RoundingMode.HALF_UP);
        return amount.doubleValue();
    }

    public static <T> T getDefaultIfNull(Supplier<T> getFunction, T defaultValue) {
        try {
            return getFunction.get() != null ? getFunction.get() : defaultValue;
        } catch (NullPointerException ex) {
            return defaultValue;
        }
    }

    public Float getAvg(Integer totalRating, Integer totalRatingCount, int roundToPlaces) {

        Float avgRating = null;

        if (Objects.nonNull(totalRating) && Objects.nonNull(totalRatingCount) && totalRatingCount > 0) {
            avgRating = getAvg((float) totalRating, totalRatingCount, roundToPlaces);
        }

        return avgRating;
    }

    public Float getAvg(Float totalRating, Integer totalRatingCount, int roundToPlaces) {

        Float avgRating = null;

        if (Objects.nonNull(totalRating) && Objects.nonNull(totalRatingCount) && totalRatingCount > 0) {
            avgRating = (totalRating) / totalRatingCount;

            avgRating = StanzaUtils.roundToPlaces(avgRating, roundToPlaces);
        }

        return avgRating;
    }

    public String getTimeStampConcatenatedFileName(String fileName) {
        return new Date().getTime() + "-" + fileName.replace(" ", "_");
    }

    public String getTimeStampConcatenatedFileName(MultipartFile multipartFile) {
        return getTimeStampConcatenatedFileName(multipartFile.getOriginalFilename());
    }

    public Double sanitize(Double amount) {
        return Objects.nonNull(amount) ? amount : NumberUtils.DOUBLE_ZERO;
    }

    public Double sanitizeAndRound2Places(Double amount) {
        return Objects.nonNull(amount) ? roundToPlaces(amount, 2) : NumberUtils.DOUBLE_ZERO;
    }

    public Boolean allEmptyCollections(Collection<?>... collections) {
        return Stream.of(collections).allMatch(CollectionUtils::isEmpty);
    }

    public Boolean anyNotEmptyCollection(Collection<?>... collections) {
        return Stream.of(collections).anyMatch(CollectionUtils::isNotEmpty);
    }

    public double getAbsoluteValue(Double value) {
        if (Objects.isNull(value)) {
            return NumberUtils.DOUBLE_ZERO;
        }
        return Math.abs(value);
    }

    public double getMedianValue(List<Double> numbers) {
        if (CollectionUtils.isEmpty(numbers)) {
             return 0 ;
        }

        List<Double> sortedNumbers = numbers.stream()
                .sorted()
                .toList();

        int size = sortedNumbers.size();

        if (size % 2 == 1) {
            return sortedNumbers.get(size / 2);
        } else {
            return (sortedNumbers.get(size / 2 - 1) + sortedNumbers.get(size / 2)) / 2.0;
        }
    }

}