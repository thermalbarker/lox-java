package com.craftinginterpreters.lox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.TypedArgumentConverter;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;


class LoxTest {

    @BeforeEach
    void setUp() {
    }

    @ParameterizedTest
    @MethodSource("allFilesInDir")
    void testInterpreter(@ConvertWith(FileConverter.class) String input) {
        Lox.run(input);
    }

    private static Path getResourcePath() {
        String s = LoxTest.class.getClassLoader().getResource("lox").getFile();
        File file = new File(s);
        return file.toPath();
    }

    public static Stream<?> allFilesInDir() throws IOException {
        return Files.list(getResourcePath());
    }

    static class FileConverter extends TypedArgumentConverter<Path, String> {
        protected FileConverter() {
            super(Path.class, String.class);
        }

        @Override
        protected String convert(Path source) throws ArgumentConversionException {
            try {
                System.out.println(source);
                return Files.readString(source, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new ArgumentConversionException(e.getMessage());
            }
        }
    }
}