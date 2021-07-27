package ru.sstu.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public enum Operation { // Типы операций и методы для каждой из них

    PLUS("Сумма") {
        @Override
        public BigDecimal resolve(BigDecimal firstNumber, BigDecimal secondNumber) {
            return firstNumber.add(secondNumber);
        }
    },
    SUBTRACT("Разность") {
        @Override
        public BigDecimal resolve(BigDecimal firstNumber, BigDecimal secondNumber) {
            return firstNumber.subtract(secondNumber);
        }

    },
    MULTIPLY("Произведение") {
        @Override
        public BigDecimal resolve(BigDecimal firstNumber, BigDecimal secondNumber) {
            return firstNumber.multiply(secondNumber);
        }
    },
    DIVIDE("Частное") {
        @Override
        public BigDecimal resolve(BigDecimal firstNumber, BigDecimal secondNumber) {
            if(secondNumber.compareTo(BigDecimal.ZERO) == 0) return null;
            return firstNumber.divide(secondNumber, new MathContext(3, RoundingMode.FLOOR));
        }
    },
    MODULE("Остаток от деления") {
        @Override
        public BigDecimal resolve(BigDecimal firstNumber, BigDecimal secondNumber) {
            return new BigDecimal(firstNumber.toBigInteger().mod(secondNumber.toBigInteger()));
        };
    };


    // Содержимое данного поля нужно для сохранения его в модель и последующего отображения на странице
    private final String localizedValue;

    Operation(String value) {
        this.localizedValue = value;
    }

    public String getLocalizedValue() {
        return localizedValue;
    }

    public abstract BigDecimal resolve(BigDecimal firstNumber, BigDecimal secondNumber);
}
