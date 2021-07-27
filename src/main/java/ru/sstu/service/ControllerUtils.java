package ru.sstu.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class ControllerUtils {
    private final TemplateEngine templateEngine;

    public ControllerUtils(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    // Данный метод преобразует объект класса Enumeration в объект класса Stream для вызова метода forEach,
    // т.к Enumeration, как и Iterator, Spliterator не имеют данного метода
    private static <T> Stream<T>
    getStreamFromIterator(Iterator<T> iterator) {
        Spliterator<T>
                spliterator = Spliterators
                .spliteratorUnknownSize(iterator, 0);

        return StreamSupport.stream(spliterator, false);
    }


    // Метод формирования строкового представления данных запроса
    public String getStringInfo(HttpServletRequest request, Model model) {
        StringBuilder res = new StringBuilder("\n");

        res.append(request.getMethod()).append(" ").append(request.getRequestURL()).append("\n");
        res.append(request.getProtocol()).append("\n");
        getStreamFromIterator(request.getHeaderNames().asIterator()).forEach(
                it -> res.append(it).append(": ").append(request.getHeader(it)).append("\n")
        );

        // Переменная контекста, хранящая в себе переменные модели и используемая далее в TemplateEngine
        Context context = new Context();
        context.setVariables(model.asMap());

        // Запись содержимого веб-страницы с учётом переменных модели
        // Шаблонные литералы в файле html с именем, соответствующим ключу в модели, заполняются значениями по соотвествующему ключу
        String finalHtml = templateEngine.process("calculator", context);

        res.append("\n").append(finalHtml).append("\n");

        return res.toString();
    }

    // Метод записи строкового представления данных запроса в файл
    public void saveInfo(String info, String fileName) throws IOException {
        try {
            OutputStream outputStream = new FileOutputStream(fileName);

            for (char i : info.toCharArray()) {
                outputStream.write(i);
            }

            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
