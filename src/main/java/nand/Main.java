package nand;

import nand.assembler.HackAssembler;
import nand.jack_analyzer.JackAnalyzer;
import nand.jack_compiler.JackCompiler;
import nand.vm_translator.VMTranslator;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static final int ASSEMBLER = 0;
    public static final int VM_TRANSLATOR = 1;
    public static final int JACK_ANALYZER = 2;
    public static final int JACK_COMPILER = 3;

    public static void main(String[] args) {
        List<String> cmdArguments = Arrays.asList(args);

        if (cmdArguments.isEmpty()) {
            usage();
            return;
        }

        String inputFile = "";
        String outputFile = "";
        int toolChosen = ASSEMBLER; // Runs as assembly by default

        for (int i = 0; i < cmdArguments.size(); i++){
            switch (cmdArguments.get(i)) {
                case "-i", "--input" -> inputFile = i + 1 != cmdArguments.size() ? cmdArguments.get(i + 1) : "";
                case "-o", "--output" -> outputFile = i + 1 != cmdArguments.size() ? cmdArguments.get(i + 1) : "";
                case "vmTranslator" -> toolChosen = VM_TRANSLATOR;
                case "jackAnalyzer" -> toolChosen = JACK_ANALYZER;
                case "jackCompiler" -> toolChosen = JACK_COMPILER;
                case "-h", "--help" -> {
                    usage();
                    return;
                }
            }
        }

        if (inputFile.isEmpty()) {
            System.out.println("No input file specified");
            usage();
            return;
        }

        try {
            switch (toolChosen) {
                case ASSEMBLER: {
                    System.out.println("Assembler translation started\r");
                    HackAssembler.translate(inputFile, outputFile);
                }
                case VM_TRANSLATOR: {
                    System.out.println("VM code translation started\r");
                    VMTranslator.translate(inputFile, outputFile);
                }
                case JACK_ANALYZER: {
                    System.out.println("Jack analyzer started\r");
                    JackAnalyzer.translate(inputFile);
                }
                case JACK_COMPILER: {
                    System.out.println("Jack compiler started\r");
                    JackCompiler.translate(inputFile);
                }

                System.out.printf("Translation ended. Output in file %s%n", outputFile);
            }

        } catch (FileNotFoundException e) {
            System.err.println("Specified file is not found");

        } catch (Exception e) {
            System.err.println("Failed to perform operation");
            e.printStackTrace();
        }
    }

    private static void usage() {
        System.out.println("""
                    Assembler of Hack machine language translator, virtual machine code and jack compilation engine
                    Based on https://www.nand2tetris.org/
                    Made by Martins P, 2023
                     
                    Arguments:
                        assembler
                           [-i] | --input      : filename of input (.asm) file
                           [-o] | --output     : filename of output (.hack) file
                        vmTranslator
                           [-i] | --input      : filename of input (.vm) file or folder containing them
                           [-o] | --output     : filename of output (.asm) file
                        jackAnalyzer
                           [-i] | --input      : filename of input (.jack) file or folder containing them
                        jackCompiler
                           [-i] | --input      : filename of input (.jack) file or folder containing them
                           
                           -h   | --help       : display this help message
                    """);
    }
}