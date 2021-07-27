package ru.sstu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sstu.service.ControllerUtils;
import ru.sstu.model.Operation;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;

@Controller // Обозначаем класс как контроллер
@RequestMapping("/calculator") // Маппинг по URL localhost:8080/calculator
public class CalculatorController {
    @Autowired
    private ControllerUtils controllerUtils;

    @GetMapping // Метод для GET запросов
    public String calculatorPage(HttpServletRequest request, Model model) throws IOException {

        // Сохранение данных запроса в файл
        controllerUtils.saveInfo(controllerUtils.getStringInfo(request, model), "HTTPInfo.txt");

        return "calculator"; // Возврат html-страницы
    }

    @PostMapping // Метод для POST запросов
    public String resolve(
            @RequestParam(required = false) Operation operation,
            @RequestParam BigDecimal firstNumber,
            @RequestParam BigDecimal secondNumber,
            Model model,
            HttpServletRequest request
    ) throws Exception {
        String errorText;

        BigDecimal result = operation.resolve(firstNumber, secondNumber);

        // При делении на 0 метод возвращает null, соответствующий текст отображается в строке результата
        if (result == null) {
            errorText = "Невозможно деление на 0";
            model.addAttribute("result", errorText);

            // Либо записывается результат деления
        } else model.addAttribute("result", result);

        // Сохранение данных в модель для отображения на странице
        model.addAttribute("firstNumber", firstNumber);
        model.addAttribute("secondNumber", secondNumber);
        model.addAttribute("operation", operation.getLocalizedValue());

        // Сохранение данных запроса в файл
        controllerUtils.saveInfo(controllerUtils.getStringInfo(request, model), "HTTPInfo.txt");

        return "calculator"; // Возврат html-страницы
    }

}
