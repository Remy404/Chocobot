package com.springboot.MyTodoList.controller;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import com.springboot.MyTodoList.service.ToDoItemService;
import com.springboot.MyTodoList.util.BotCommands;
import com.springboot.MyTodoList.util.BotHelper;
import com.springboot.MyTodoList.util.BotLabels;
import com.springboot.MyTodoList.util.BotMessages;

public class ToDoItemBotController extends TelegramLongPollingBot {

	private static final Logger logger = LoggerFactory.getLogger(ToDoItemBotController.class);
	private ToDoItemService toDoItemService;
	private String botName;

	public ToDoItemBotController(String botToken, String botName, ToDoItemService toDoItemService) {
		super(botToken);
		logger.info("Bot Token: " + botToken);
		logger.info("Bot name: " + botName);
		this.toDoItemService = toDoItemService;
		this.botName = botName;
	}

	private Map<Long, String> pendingDescriptions = new HashMap<>();
	private Map<Long, Integer> awaitingStorypoints = new HashMap<>();
	private Map<Long, String> awaitingResponsable = new HashMap<>();
	private Map<Long, String> awaitingPriority = new HashMap<>();
	private Map<Long, Integer> awaitingEstimatedHours = new HashMap<>();
	private Map<Long, String> awaitingExpirationDate = new HashMap<>();
	private Map<Long, String> awaitingEstado = new HashMap<>();

	private static final Map<String, String> allowedUsers = Map.of(
			"A00833409@tec.mx", "Francisco",
			"A01383541@tec.mx", "Facundo",
			"A01174206@tec.mx", "Alejandro",
			"A00831554@tec.mx", "Saúl");

	private Map<Long, String> loggedInUsers = new HashMap<>();

	private OffsetDateTime parseDateString(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");

		// Parsear a LocalDateTime
		LocalDateTime localDate = LocalDateTime.parse(dateString, formatter);

		// Convertir a OffsetDateTime (puedes especificar el offset deseado, en este
		// caso UTC+0)
		OffsetDateTime offsetDate = localDate.atOffset(ZoneOffset.UTC);

		return offsetDate;
	}

	@Override
	public void onUpdateReceived(Update update) {

		if (update.hasMessage() && update.getMessage().hasText()) {

			String messageTextFromTelegram = update.getMessage().getText();
			long chatId = update.getMessage().getChatId();

			if (messageTextFromTelegram.equals(BotCommands.START_COMMAND.getCommand())
					|| messageTextFromTelegram.equals(BotLabels.SHOW_MAIN_SCREEN.getLabel())) {

				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotMessages.HELLO_MYTODO_BOT.getMessage());

				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				// first row
				KeyboardRow row = new KeyboardRow();
				row.add(BotLabels.LIST_ALL_ITEMS.getLabel());
				row.add(BotLabels.ADD_NEW_ITEM.getLabel());
				// Add the first row to the keyboard
				keyboard.add(row);

				// second row
				row = new KeyboardRow();
				row.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
				row.add(BotLabels.HIDE_MAIN_SCREEN.getLabel());
				keyboard.add(row);

				row = new KeyboardRow();
				row.add(BotLabels.LOGIN.getLabel());
				row.add(BotLabels.LOGOUT.getLabel());
				keyboard.add(row);

				// Set the keyboard
				keyboardMarkup.setKeyboard(keyboard);

				// Add the keyboard markup
				messageToTelegram.setReplyMarkup(keyboardMarkup);

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (messageTextFromTelegram.indexOf(BotLabels.DONE.getLabel()) != -1) {

				String done = messageTextFromTelegram.substring(0,
						messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
				Integer id = Integer.valueOf(done);

				try {

					ToDoItem item = getToDoItemById(id).getBody();
					item.setDone(true);
					updateToDoItem(item, id);
					BotHelper.sendMessageToTelegram(chatId, BotMessages.ITEM_DONE.getMessage(), this);

				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (messageTextFromTelegram.indexOf(BotLabels.DELETE.getLabel()) != -1) {

				// Extraemos el ID de la tarea que se desea eliminar
				String delete = messageTextFromTelegram.substring(0,
						messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
				Integer id = Integer.valueOf(delete);

				try {
					// Intentamos eliminar el ítem usando el servicio
					boolean deleted = toDoItemService.deleteToDoItem(id);

					// Verificamos si la eliminación fue exitosa
					if (deleted) {
						BotHelper.sendMessageToTelegram(chatId, "The item has been successfully deleted.", this);
						BotHelper.sendMessageToTelegram(chatId,
								"Return to the complete task list with the /todolist command.", this);
					} else {
						BotHelper.sendMessageToTelegram(chatId, "Failed to delete the item. Please try again.", this);
					}
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
					BotHelper.sendMessageToTelegram(chatId, "An error occurred while trying to delete the item.", this);
				}

			} else if (messageTextFromTelegram.indexOf(BotLabels.UNDO.getLabel()) != -1) {

				// Extraemos el ID de la tarea que se desea cambiar a "UNDONE"
				String undo = messageTextFromTelegram.substring(0,
				messageTextFromTelegram.indexOf(BotLabels.DASH.getLabel()));
				Integer id = Integer.valueOf(undo);
	
		try {
			// Intentamos obtener el ítem por ID
			ToDoItem item = toDoItemService.getItemById(id).getBody();
	
			// Verificamos si el ítem se encontró y si está marcado como "DONE"
			if (item != null && item.isDone()) {
				item.setDone(false); // Cambiamos el estado a "UNDONE"
				toDoItemService.updateToDoItem(id, item); // Actualizamos en la base de datos
	
				// Enviar mensaje de confirmación al usuario
				BotHelper.sendMessageToTelegram(chatId, "The item has been marked as undone.", this);
	
				// Obtener todos los ítems y filtrar los activos
				List<ToDoItem> allItems = toDoItemService.findAll(); // Obtener todos los ítems
				List<ToDoItem> activeItems = allItems.stream()
					.filter(activeItem -> !activeItem.isDone()) // Cambié el nombre del parámetro aquí
					.collect(Collectors.toList());
	
				// Reconstruimos el teclado para mostrar los ítems activos
				ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup(); // Usar ReplyKeyboardMarkup de la librería de Telegram
				keyboard.setResizeKeyboard(true); // Para ajustar el tamaño del teclado
				keyboard.setSelective(true); // Para que el teclado se muestre solo a este chat
	
				List<KeyboardRow> rows = new ArrayList<>(); // Lista para las filas del teclado
	
				for (ToDoItem activeItem : activeItems) {
					KeyboardRow currentRow = new KeyboardRow(); // Crear una nueva fila
					currentRow.add(activeItem.getDescription() + " " + BotLabels.DASH.getLabel() + " " + activeItem.getAssigned());
					currentRow.add(activeItem.getID() + BotLabels.DASH.getLabel() + BotLabels.DONE.getLabel());
					currentRow.add(BotLabels.EDIT.getLabel());
					rows.add(currentRow); // Agregar la fila a la lista de filas
				}
	
				keyboard.setKeyboard(rows); // Asignar las filas al teclado
	
				// Enviar el nuevo teclado al usuario
				BotHelper.sendKeyboardToTelegram(chatId, keyboard, this);
			} else {
				// Si el ítem no se encontró o no estaba hecho
				BotHelper.sendMessageToTelegram(chatId, "The item is either not found or is not done.", this);
			}
	
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			BotHelper.sendMessageToTelegram(chatId,
					"An error occurred while trying to mark the item as undone.", this);
		}

			} else if (messageTextFromTelegram.equals(BotCommands.HIDE_COMMAND.getCommand())
					|| messageTextFromTelegram.equals(BotLabels.HIDE_MAIN_SCREEN.getLabel())) {

				BotHelper.sendMessageToTelegram(chatId, BotMessages.BYE.getMessage(), this);

			} else if (messageTextFromTelegram.equals(BotCommands.TODO_LIST.getCommand())
					|| messageTextFromTelegram.equals(BotLabels.LIST_ALL_ITEMS.getLabel())
					|| messageTextFromTelegram.equals(BotLabels.MY_TODO_LIST.getLabel())) {

				List<ToDoItem> allItems = getAllToDoItems();
				ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
				List<KeyboardRow> keyboard = new ArrayList<>();

				// command back to main screen
				KeyboardRow mainScreenRowTop = new KeyboardRow();
				mainScreenRowTop.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
				keyboard.add(mainScreenRowTop);

				KeyboardRow firstRow = new KeyboardRow();
				firstRow.add(BotLabels.ADD_NEW_ITEM.getLabel());
				keyboard.add(firstRow);

				KeyboardRow myTodoListTitleRow = new KeyboardRow();
				myTodoListTitleRow.add(BotLabels.MY_TODO_LIST.getLabel());
				keyboard.add(myTodoListTitleRow);

				List<ToDoItem> activeItems = allItems.stream().filter(item -> item.isDone() == false)
						.collect(Collectors.toList());

				for (ToDoItem item : activeItems) {
					KeyboardRow currentRow = new KeyboardRow();
					currentRow.add(item.getDescription() + " " + BotLabels.DASH.getLabel() + " " + item.getAssigned());
					currentRow.add(item.getID() + BotLabels.DASH.getLabel() + BotLabels.DONE.getLabel());
					currentRow.add(BotLabels.EDIT.getLabel());
					keyboard.add(currentRow);
				}

				List<ToDoItem> doneItems = allItems.stream().filter(item -> item.isDone() == true)
						.collect(Collectors.toList());

				for (ToDoItem item : doneItems) {
					KeyboardRow currentRow = new KeyboardRow();
					currentRow.add(item.getDescription() + " " + BotLabels.DASH.getLabel() + " " + item.getAssigned());
					// currentRow.add(item.getDescription() == null ? "No desc" :
					// item.getDescription());
					currentRow.add(item.getID() + BotLabels.DASH.getLabel() + BotLabels.UNDO.getLabel());
					currentRow.add(item.getID() + BotLabels.DASH.getLabel() + BotLabels.DELETE.getLabel());
					currentRow.add(BotLabels.EDIT.getLabel());
					keyboard.add(currentRow);
				}
				// command back to main screen
				KeyboardRow mainScreenRowBottom = new KeyboardRow();
				mainScreenRowBottom.add(BotLabels.SHOW_MAIN_SCREEN.getLabel());
				keyboard.add(mainScreenRowBottom);

				keyboardMarkup.setKeyboard(keyboard);

				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(BotLabels.MY_TODO_LIST.getLabel());
				messageToTelegram.setReplyMarkup(keyboardMarkup);

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (messageTextFromTelegram.equals(BotCommands.ADD_ITEM.getCommand())
					|| messageTextFromTelegram.equals(BotLabels.ADD_NEW_ITEM.getLabel())) {
				try {
					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(BotMessages.TYPE_NEW_TODO_ITEM.getMessage());
					// hide keyboard
					ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove(true);
					messageToTelegram.setReplyMarkup(keyboardMarkup);

					// send message
					execute(messageToTelegram);

					pendingDescriptions.remove(chatId);
					awaitingStorypoints.remove(chatId);
					awaitingResponsable.remove(chatId);
					awaitingPriority.remove(chatId);
					awaitingEstimatedHours.remove(chatId);
					awaitingEstado.remove(chatId);
					awaitingExpirationDate.remove(chatId);

				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (messageTextFromTelegram.equals(BotCommands.LOGIN_COMMAND.getCommand())
					|| messageTextFromTelegram.equals(BotLabels.LOGIN.getLabel())) {

				SendMessage messageToTelegram = new SendMessage();
				messageToTelegram.setChatId(chatId);
				messageToTelegram.setText(
						"Please enter your email followed by your name separated by a dash (e.g: example@email - name):");

				try {
					execute(messageToTelegram);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else if (messageTextFromTelegram.contains("-")) {
				String[] parts = messageTextFromTelegram.split(" - ");
				if (parts.length == 2) {
					String email = parts[0].trim();
					String name = parts[1].trim();

					if (allowedUsers.containsKey(email)) {
						String registeredName = allowedUsers.get(email);

						if (registeredName.equals(name)) {
							// Almacenar el nombre del usuario después del login exitoso
							loggedInUsers.put(chatId, name);

							SendMessage messageToTelegram = new SendMessage();
							messageToTelegram.setChatId(chatId);

							List<ToDoItem> userTasks = toDoItemService.findByAssignedName(name);

							if (userTasks.isEmpty()) {
								messageToTelegram.setText("You have no tasks assigned.");
							} else {
								StringBuilder tasksMessage = new StringBuilder("Welcome " + name + "!"
										+ " You have successfully logged in." + "\n\nHere are your current tasks:\n\n");
								for (ToDoItem item : userTasks) {
									tasksMessage.append("- ")
											.append(item.getDescription())
											.append(" (Priority: ").append(item.getPriority()).append(" - ")
											.append(" StoryPoints: ").append(item.getStoryPoints()).append(" - ")
											.append(" Status: ").append(item.getEstado())
											.append(")\n");
								}
								messageToTelegram.setText(tasksMessage.toString());
							}

							try {
								execute(messageToTelegram);
							} catch (TelegramApiException e) {
								logger.error(e.getLocalizedMessage(), e);
							}
						} else {
							SendMessage messageToTelegram = new SendMessage();
							messageToTelegram.setChatId(chatId);
							messageToTelegram.setText("Invalid credentials. Please enter a valid email and name.");

							try {
								execute(messageToTelegram);
							} catch (TelegramApiException e) {
								logger.error(e.getLocalizedMessage(), e);
							}
						}
					} else {
						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram.setText("Unauthorized user. Please use a registered email address.");

						try {
							execute(messageToTelegram);
						} catch (TelegramApiException e) {
							logger.error(e.getLocalizedMessage(), e);
						}
					}
				} else {
					SendMessage messageToTelegram = new SendMessage();
					messageToTelegram.setChatId(chatId);
					messageToTelegram.setText(
							"Invalid format. Please enter your email followed by your name separated by a dash (e.g: example@email - name).");

					try {
						execute(messageToTelegram);
					} catch (TelegramApiException e) {
						logger.error(e.getLocalizedMessage(), e);
					}
				}

				// Nueva condición para el botón y comando /logout
			} else if (messageTextFromTelegram.equals(BotCommands.LOGOUT_COMMAND.getCommand())
					|| messageTextFromTelegram.equals(BotLabels.LOGOUT.getLabel())) {
				// Obtener el nombre del usuario para el mensaje de despedida
				String name = loggedInUsers.get(chatId);

				SendMessage logoutMessage = new SendMessage();
				logoutMessage.setChatId(chatId);
				logoutMessage.setText("Your session has ended successfully " + name + ".");

				// Limpiar la información de sesión
				loggedInUsers.remove(chatId);
				pendingDescriptions.remove(chatId);
				awaitingStorypoints.remove(chatId);
				awaitingResponsable.remove(chatId);
				awaitingPriority.remove(chatId);
				awaitingEstimatedHours.remove(chatId);
				awaitingEstado.remove(chatId);
				awaitingExpirationDate.remove(chatId);

				try {
					execute(logoutMessage);
				} catch (TelegramApiException e) {
					logger.error(e.getLocalizedMessage(), e);
				}

			} else {
				try {
					if (awaitingExpirationDate.getOrDefault(chatId, null) != null) {
						// If awaiting expiration date, we process it and proceed to create the new item
						try {
							OffsetDateTime offsetDate = parseDateString(messageTextFromTelegram);
							awaitingExpirationDate.put(chatId, offsetDate.toString());

							// Create new item with all fields collected
							ToDoItem newItem = new ToDoItem();
							newItem.setDescription(pendingDescriptions.get(chatId));
							newItem.setStoryPoints(awaitingStorypoints.get(chatId));
							newItem.setAssigned(awaitingResponsable.get(chatId));
							newItem.setPriority(awaitingPriority.get(chatId));
							newItem.setEstimated_Hours(awaitingEstimatedHours.get(chatId));
							newItem.setEstado(awaitingEstado.get(chatId));
							newItem.setExpiration_TS(offsetDate);
							newItem.setCreation_ts(OffsetDateTime.now());
							newItem.setDone(false);

							// Save the item
							ResponseEntity entity = addToDoItem(newItem);

							// Send confirmation message
							SendMessage messageToTelegram = new SendMessage();
							messageToTelegram.setChatId(chatId);
							messageToTelegram.setText("New item added:\nDescription: " + newItem.getDescription()
									+ "\nStoryPoints: " + newItem.getStoryPoints()
									+ "\nResponsable: " + newItem.getAssigned()
									+ "\nPriority: " + newItem.getPriority()
									+ "\nEstimated Hours: " + newItem.getEstimated_Hours()
									+ "\nStatus: " + newItem.getEstado()
									+ "\nExpiration Date: " + newItem.getExpiration_TS()
									+ "\nCheck your pending tasks /todolist");
							execute(messageToTelegram);

							// Clear all pending states
							pendingDescriptions.remove(chatId);
							awaitingStorypoints.remove(chatId);
							awaitingResponsable.remove(chatId);
							awaitingPriority.remove(chatId);
							awaitingEstimatedHours.remove(chatId);
							awaitingEstado.remove(chatId);
							awaitingExpirationDate.remove(chatId);

						} catch (DateTimeParseException e) {
							// If expiration date format is invalid
							SendMessage errorMessage = new SendMessage();
							errorMessage.setChatId(chatId);
							errorMessage.setText(
									"Please enter a valid expiration date with the following format YYYY/MM/DD HH:MM");
							execute(errorMessage);
							return;
						}

					} else if (awaitingEstado.getOrDefault(chatId, null) != null) {
						// Clear the pending states
						awaitingExpirationDate.put(chatId, "");
						awaitingEstado.put(chatId, messageTextFromTelegram);

						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram
								.setText("Please enter the expiration date with the following format YYYY/MM/DD HH:MM");
						execute(messageToTelegram);

					} else if (awaitingEstimatedHours.getOrDefault(chatId, null) != null) {
						// Clear the pending states
						awaitingEstado.put(chatId, "");

						SendMessage errorMessage = new SendMessage();
						try {
							int estimatedHours = Integer.parseInt(messageTextFromTelegram);
							if (estimatedHours < 1) {
								errorMessage.setChatId(chatId);
								errorMessage
										.setText("The number of estimated hourse has to be above 1. Please try again.");
								execute(errorMessage);
							}

							awaitingEstimatedHours.put(chatId, estimatedHours);
						} catch (NumberFormatException e) {
							errorMessage.setChatId(chatId);
							errorMessage.setText("Please enter a number for the estimated hours");
							execute(errorMessage);
						}

						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram
								.setText(
										"Please enter the current status for this task: \n(To Do / In Progress / Completed)");
						execute(messageToTelegram);

					} else if (awaitingPriority.getOrDefault(chatId, null) != null) {
						// Clear the pending states
						awaitingEstimatedHours.put(chatId, 0);
						awaitingPriority.put(chatId, messageTextFromTelegram);

						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram.setText("Please enter the estimated hours for this task");
						execute(messageToTelegram);

					}
					// Check if we're waiting for responsable
					else if (awaitingResponsable.getOrDefault(chatId, null) != null) {
						// Clear the pending states
						awaitingPriority.put(chatId, "");
						awaitingResponsable.put(chatId, messageTextFromTelegram);

						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram
								.setText("Please enter the designated priority (Low / Mid / High) for this task");
						execute(messageToTelegram);
					}
					// Check if we're waiting for storypoints
					else if (awaitingStorypoints.getOrDefault(chatId, null) != null) {
						try {
							int storypoints = Integer.parseInt(messageTextFromTelegram);
							if (storypoints < 0 || storypoints > 8) {
								SendMessage errorMessage = new SendMessage();
								errorMessage.setChatId(chatId);
								errorMessage.setText("Please enter a valid number between (1 and 8) for story points.");
								execute(errorMessage);
								return;
							}

							// Store storypoints and ask for responsable
							awaitingStorypoints.put(chatId, storypoints);
							awaitingResponsable.put(chatId, "");

							SendMessage messageToTelegram = new SendMessage();
							messageToTelegram.setChatId(chatId);
							messageToTelegram.setText("Please enter the name of the responsible developer:");
							execute(messageToTelegram);

						} catch (NumberFormatException e) {
							SendMessage errorMessage = new SendMessage();
							errorMessage.setChatId(chatId);
							errorMessage.setText("Please enter a valid number for story points.");
							execute(errorMessage);
						}
					} else {
						// This is the first message - save description and ask for storypoints
						pendingDescriptions.put(chatId, messageTextFromTelegram);
						awaitingStorypoints.put(chatId, 0);

						SendMessage messageToTelegram = new SendMessage();
						messageToTelegram.setChatId(chatId);
						messageToTelegram.setText("Please enter story points (1-8) for this task:");
						execute(messageToTelegram);
					}
				} catch (Exception e) {
					logger.error(e.getLocalizedMessage(), e);
					try {
						// Clear all pending states in case of error
						pendingDescriptions.remove(chatId);
						awaitingStorypoints.remove(chatId);
						awaitingResponsable.remove(chatId);
						awaitingPriority.remove(chatId);
						awaitingEstimatedHours.remove(chatId);
						awaitingEstado.remove(chatId);
						awaitingExpirationDate.remove(chatId);

						SendMessage errorMessage = new SendMessage();
						errorMessage.setChatId(chatId);
						errorMessage.setText("An error occurred. Please try again.");
						execute(errorMessage);
					} catch (TelegramApiException ex) {
						logger.error(ex.getLocalizedMessage(), ex);
					}
				}
			}

		}

	}

	@Override
	public String getBotUsername() {
		return botName;
	}

	// GET /todolist
	public List<ToDoItem> getAllToDoItems() {
		return toDoItemService.findAll();
	}

	// GET BY ID /todolist/{id}
	public ResponseEntity<ToDoItem> getToDoItemById(@PathVariable int id) {
		try {
			ResponseEntity<ToDoItem> responseEntity = toDoItemService.getItemById(id);
			return new ResponseEntity<ToDoItem>(responseEntity.getBody(), HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// PUT /todolist
	public ResponseEntity addToDoItem(@RequestBody ToDoItem todoItem) throws Exception {
		ToDoItem td = toDoItemService.addToDoItem(todoItem);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("location", "" + td.getID());
		responseHeaders.set("Access-Control-Expose-Headers", "location");
		// URI location = URI.create(""+td.getID())

		return ResponseEntity.ok().headers(responseHeaders).build();
	}

	// UPDATE /todolist/{id}
	public ResponseEntity updateToDoItem(@RequestBody ToDoItem toDoItem, @PathVariable int id) {
		try {
			ToDoItem toDoItem1 = toDoItemService.updateToDoItem(id, toDoItem);
			System.out.println(toDoItem1.toString());
			return new ResponseEntity<>(toDoItem1, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	// DELETE todolist/{id}
	public ResponseEntity<Boolean> deleteToDoItem(@PathVariable("id") int id) {
		Boolean flag = false;
		try {
			flag = toDoItemService.deleteToDoItem(id);
			return new ResponseEntity<>(flag, HttpStatus.OK);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(flag, HttpStatus.NOT_FOUND);
		}
	}

}