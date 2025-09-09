package com.edu.uptc.parkingSystem.interfaces;

import com.edu.uptc.parkingSystem.enums.ETypeFileEnum;

public interface IActionsFile {
	public void loadFile(ETypeFileEnum eTypeFileEnum);
	public void dumpFile(ETypeFileEnum eTypeFileEnum);
}
