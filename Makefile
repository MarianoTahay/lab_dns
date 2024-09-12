# Variables
JAVAC = javac
JAVA = java
SRC_DIR = src
TARGET_DIR = target

# Regla por defecto
all: run_main

# Compilar Main.java y ejecutar
compile_main: $(TARGET_DIR)/Main.class

$(TARGET_DIR)/Main.class: $(SRC_DIR)/Main.java
	@echo "Compilando Main.java..."
	$(JAVAC) -d $(TARGET_DIR) $(SRC_DIR)/Main.java

run_main: compile_main
	@echo "Ejecutando Main..."
	$(JAVA) -cp $(TARGET_DIR) Main

# Limpiar archivos compilados
clean:
	@echo "Limpiando archivos compilados..."
	del /Q $(TARGET_DIR)\*.class