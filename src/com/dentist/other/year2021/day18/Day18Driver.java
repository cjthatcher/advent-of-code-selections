package com.dentist.other.year2021.day18;

import com.dentist.other.kafka.InputConsumer;
import com.dentist.other.kafka.InputProducer;

import java.io.IOException;

public class Day18Driver {

    static SnailNumber snailNumber;
    static int linesConsumed = 0;
    private static final String IP_ADDRESS = "172.25.211.67:9092";

    public static void main(String[] args) {
        InputProducer producer = new InputProducer("resources/2021/18.txt", "2021-18",IP_ADDRESS);
        try {
            producer.produceData();
        } catch (IOException e) {
            throw new RuntimeException("Something exploded while trying to produce lines to the topic.", e);
        }


        InputConsumer consumer = new InputConsumer("2021-18", IP_ADDRESS) {
            @Override
            public void handleLine(String s) {
                if (snailNumber == null) {
                    snailNumber = SnailNumber.fromString(s);
                }

                snailNumber = snailNumber.add(SnailNumber.fromString(s)).reduce();
                System.err.println("Consumed " + ++linesConsumed + "lines: Magnitude is " + snailNumber.magnitude());
            }
        };

        consumer.beginConsumption();
    }
}
