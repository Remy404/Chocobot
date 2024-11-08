package com.springboot.MyTodoList.util;

public enum BotMessages {
  
	HELLO_MYTODO_BOT("Hello! I'm ChocoBot!\nPlease sing in with the /login command or button."),
	BOT_REGISTERED_STARTED("Bot registered and started succesfully!"),
	ITEM_DONE("Task marked as done successfully!\nSelect /todolist to return to the complete list of items."), 
	ITEM_UNDONE("Item undone! Select /todolist to return to the list of todo items, or /start to go to the main screen."), 
	ITEM_DELETED("Item deleted! Select /todolist to return to the list of todo items, or /start to go to the main screen."),
	TYPE_NEW_TODO_ITEM("Type the description of the new item.\nPress the send send button (blue arrow)."),
	NEW_ITEM_ADDED("New item added! Select /todolist to return to the list of todo items, or /start to go to the main screen."),
	LOGIN("Please enter your email followed by your name separated by a dash (e.g: example@email - name):"),
	LOGOUT("Your session has ended successfully"),
	ITEM_EDIT("Edit task"), 
	BYE("Farewell!\nSelect the command /start to resume your activity!");

	private String message;

	BotMessages(String enumMessage) {
		this.message = enumMessage;
	}

	public String getMessage() {
		return message;
	}

}
