package com.github.av.bytelegrambot.command;

import com.github.av.bytelegrambot.AvbyApiClient;
import com.github.av.bytelegrambot.AvbyApiClientImpl;
import com.github.av.bytelegrambot.bot.AvbyTelegramBot;
import com.github.av.bytelegrambot.repository.entity.BrandEntity;
import com.github.av.bytelegrambot.repository.entity.GenerationEntity;
import com.github.av.bytelegrambot.repository.entity.ModelEntity;
import com.github.av.bytelegrambot.service.BotMessageService;
import com.github.av.bytelegrambot.service.BrandServiceImpl;
import com.github.av.bytelegrambot.service.CarLibraryService;
import com.github.av.bytelegrambot.service.LocalizationService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class NotACommand implements Command{

    private final BotMessageService botMessageService;

    private final AvbyApiClientImpl avbyApiClient;

    private Map<Integer, BrandEntity> brandMap = new HashMap<>();
    private Map<Integer, ModelEntity> modelMap = new HashMap<>();
    private Map<Integer, GenerationEntity> generationMap = new HashMap<>();


    private final LocalizationService localizationService;

    private static final String NOT_A_COMMAND_MESSAGE_KEY = "not_a_command_message";
    private static final String BACK_COMMAND_DESCRIPTION_MESSAGE_KEY = "back_command_description_message_key";
    private static final String NO_SUCH_BRAND_MESSAGE_KEY = "no_such_brand_message";
    private static final String NO_SUCH_MODEL_MESSAGE_KEY = "no_such_model_message";
    private static final String NO_SUCH_GENERATION_MESSAGE_KEY = "no_such_generation_message";

    @Autowired
    public NotACommand(BotMessageService botMessageService, LocalizationService localizationService, AvbyApiClientImpl avbyApiClient) {
        this.botMessageService = botMessageService;
        this.localizationService = localizationService;
        this.avbyApiClient = avbyApiClient;

    }

    @Override
    public void execute(Update update) {
        botMessageService.sendMessage(update.getMessage().getChatId().toString(), localizationService.getMessage(NOT_A_COMMAND_MESSAGE_KEY));
    }

    public void execute(Update update, String state) throws NoSuchElementException, NumberFormatException{
        if(state.equals("")) {
            botMessageService.sendMessage(update.getMessage().getChatId().toString(), localizationService.getMessage(NOT_A_COMMAND_MESSAGE_KEY));
        } else if (state.equals("brand-exception")){
            botMessageService.sendMessage(update.getMessage().getChatId().toString(),localizationService.getMessage(NO_SUCH_BRAND_MESSAGE_KEY));
        } else if(state.equals("model-exception")){
            botMessageService.sendMessage(update.getMessage().getChatId().toString(),localizationService.getMessage(NO_SUCH_MODEL_MESSAGE_KEY));
        } else if(state.equals("generation-exception")){
            botMessageService.sendMessage(update.getMessage().getChatId().toString(),localizationService.getMessage(NO_SUCH_GENERATION_MESSAGE_KEY));
        } else if(state.equals("search")){

            List<BrandEntity> brands = avbyApiClient.getCarLibraryService().getBrandService().getAllBrands();
            int i=1;
            for (BrandEntity brand: brands) {
                brandMap.put(i++, brand);
            }
            avbyApiClient.setCarArgs("");
            int brandId = avbyApiClient.getCarLibraryService().getBrandService().getById(brandMap.get(Integer.parseInt(update.getMessage().getText())).getId()).get().getId();
            avbyApiClient.setCarArgs(String.valueOf(brandId));
            StringBuilder builder = new StringBuilder();
            List<ModelEntity> models = avbyApiClient.getCarLibraryService().getModelService().getAllByBrand(brandId);
            i = 1;
            for (ModelEntity model : models) {
                modelMap.put(i,model);
                builder.append(i++).append(". ").append(model.getName()).append("\n");
            }
            builder.append(localizationService.getMessage(BACK_COMMAND_DESCRIPTION_MESSAGE_KEY));
            botMessageService.sendMessage(update.getMessage().getChatId().toString(), builder.toString());

        } else if (state.equals("brand")){
            int modelId = avbyApiClient.getCarLibraryService().getModelService().getById(modelMap.get(Integer.parseInt(update.getMessage().getText())).getId()).get().getId();
            avbyApiClient.setCarArgs(avbyApiClient.getCarArgs() + " " + modelId);
            StringBuilder builder = new StringBuilder();
            List<GenerationEntity> generations = avbyApiClient.getCarLibraryService().getGenerationService().getAllByModel(modelId);
            int i = 1;
            for (GenerationEntity generation: generations) {
                generationMap.put(i,generation);
                builder.append(i++).append(". ").append(generation.getName()).append("\n");
            }
            builder.append(localizationService.getMessage(BACK_COMMAND_DESCRIPTION_MESSAGE_KEY));
            botMessageService.sendMessage(update.getMessage().getChatId().toString(), builder.toString());
        } else if (state.equals("model")){
            int genId = avbyApiClient.getCarLibraryService().getGenerationService().getById(generationMap.get(Integer.parseInt(update.getMessage().getText())).getId()).get().getId();
            avbyApiClient.setCarArgs(avbyApiClient.getCarArgs() + " " + genId);
            String[] carArgsArr = avbyApiClient.getCarArgs().split(" ");
            avbyApiClient.getAllAds(Integer.parseInt(carArgsArr[0]),Integer.parseInt(carArgsArr[1]),Integer.parseInt(carArgsArr[2]));
            for (String ads: avbyApiClient.getAdsList()) {
                botMessageService.sendMessage(update.getMessage().getChatId().toString(), ads);
            }

        }
    }
}
