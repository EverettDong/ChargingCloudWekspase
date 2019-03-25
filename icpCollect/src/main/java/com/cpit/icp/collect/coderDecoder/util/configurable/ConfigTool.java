package com.cpit.icp.collect.coderDecoder.util.configurable;



public class ConfigTool {
	
	private static final String LOG = ConfigTool.class.getName();
//	static Map<String,List<CommandInfo>> commandListMap =new HashMap<String,List<CommandInfo>>();
	/**
	 * 从一个byte[]数组中截取一部分
	 * @param src
	 * @param begin
	 * @param count
	 * @return
	 */
	public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i=begin; i<begin+count; i++) bs[i-begin] = src[i];
        return bs;
    }
	/*	
	public static List<CommandInfo> getMessageNameListByProtocal(String protocal) {
		if(commandListMap.isEmpty()){
			addMessageNameListByProtocal();
			addMessageNameListByProtocal34();
			addMessageNameListByProtocal35();
		}
		return commandListMap.get(protocal);
	}

	public static void addMessageNameListByProtocal34() {
		SAXReader sax = new SAXReader();
		Document document34 = null;
		try {
			document34 = sax
					.read(ConfigTool.class.getResourceAsStream("/conf/configurable/charging_3.4/designcharge.xml"));
			// xml 根元素 protocolset
			String expectedId = "";
			Element orderEle = null;
			Element rootEle = document34.getRootElement();
			int index = 0;
			List<CommandInfo> commandList34 = new ArrayList<CommandInfo>();
			CommandInfo info = new CommandInfo("0");
			info.setName("命令列表");
			commandList34.add(info);
			for (Iterator iter = rootEle.elementIterator(); iter.hasNext();) {
				// protocol
				index++;
				orderEle = (Element) iter.next();
				expectedId = orderEle.attributeValue("id");
				CommandInfo cmd = new CommandInfo(String.valueOf(index));
				cmd.setName(expectedId);
				commandList34.add(cmd);
			}
			commandListMap.put("充电桩协议V3.4", commandList34);
		} catch (Exception e) {
			LoggerOperator.err(LOG, "charging_3.4 error!",e);
		}
	}
	
	public static void addMessageNameListByProtocal35() {
		SAXReader sax = new SAXReader();
		Document document35 = null;
		try {
			document35 = sax
					.read(ConfigTool.class.getResourceAsStream("/conf/configurable/charging_3.5/designcharge.xml"));
			// xml 根元素 protocolset
			String expectedId = "";
			Element orderEle = null;
			Element rootEle = document35.getRootElement();
			int index = 0;
			List<CommandInfo> commandList35 = new ArrayList<CommandInfo>();
			CommandInfo info = new CommandInfo("0");
			info.setName("命令列表");
			commandList35.add(info);
			for (Iterator iter = rootEle.elementIterator(); iter.hasNext();) {
				// protocol
				index++;
				orderEle = (Element) iter.next();
				expectedId = orderEle.attributeValue("id");
				CommandInfo cmd = new CommandInfo(String.valueOf(index));
				cmd.setName(expectedId);
				commandList35.add(cmd);
			}
			commandListMap.put("充电桩协议V3.5", commandList35);
		} catch (Exception e) {
			LoggerOperator.err(LOG, "charging_3.5 error!",e);
		}
	}
	
	public static void addMessageNameListByProtocal() {
		List<CommandInfo> commandListList = new ArrayList<CommandInfo>();
		CommandInfo info0 = new CommandInfo("0");
		info0.setName("命令列表");
		CommandInfo info1 = new CommandInfo("1");
		info1.setName("0xA0");
		CommandInfo info2 = new CommandInfo("2");
		info2.setName("0xB0");
		CommandInfo info3 = new CommandInfo("3");
		info3.setName("0xA1");
		CommandInfo info4 = new CommandInfo("4");
		info4.setName("0xB3");
		CommandInfo info5 = new CommandInfo("5");
		info5.setName("0xA2");
		CommandInfo info6 = new CommandInfo("6");
		info6.setName("0xB2");
		CommandInfo info7 = new CommandInfo("7");
		info7.setName("0xA4");
		CommandInfo info8 = new CommandInfo("8");
		info8.setName("0xB4");
		CommandInfo info9 = new CommandInfo("9");
		info9.setName("0xA5");
		CommandInfo info10 = new CommandInfo("10");
		info10.setName("0xB5");
		CommandInfo info11 = new CommandInfo("11");
		info11.setName("0xA9");
		CommandInfo info12 = new CommandInfo("12");
		info12.setName("0xB9");
		CommandInfo info13 = new CommandInfo("13");
		info13.setName("0xF0");
		CommandInfo info14 = new CommandInfo("14");
		info14.setName("0xF1");
		CommandInfo info15 = new CommandInfo("15");
		info15.setName("0xF2");
		CommandInfo info16 = new CommandInfo("16");
		info16.setName("0xF3");
		CommandInfo info17 = new CommandInfo("17");
		info17.setName("0x51");
		CommandInfo info18 = new CommandInfo("18");
		info18.setName("0x65");
		CommandInfo info19 = new CommandInfo("19");
		info19.setName("0x56");
		CommandInfo info20 = new CommandInfo("20");
		info20.setName("0x4A");
		CommandInfo info21 = new CommandInfo("21");
		info21.setName("0x80");
		CommandInfo info22 = new CommandInfo("22");
		info22.setName("0x40");
		CommandInfo info23 = new CommandInfo("23");
		info23.setName("0x41");
		CommandInfo info24 = new CommandInfo("24");
		info24.setName("0x43");
		CommandInfo info25 = new CommandInfo("25");
		info25.setName("0x6C");
		CommandInfo info26 = new CommandInfo("26");
		info26.setName("0x7B");
		commandListList.add(info0);
		commandListList.add(info1);
		commandListList.add(info2);
		commandListList.add(info3);
		commandListList.add(info4);
		commandListList.add(info5);
		commandListList.add(info6);
		commandListList.add(info7);
		commandListList.add(info8);
		commandListList.add(info9);
		commandListList.add(info10);
		commandListList.add(info11);
		commandListList.add(info12);
		commandListList.add(info13);
		commandListList.add(info14);
		commandListList.add(info15);
		commandListList.add(info16);
		commandListList.add(info17);
		commandListList.add(info18);
		commandListList.add(info19);
		commandListList.add(info20);
		commandListList.add(info21);
		commandListList.add(info22);
		commandListList.add(info23);
		commandListList.add(info24);
		commandListList.add(info25);
		commandListList.add(info26);
		commandListMap.put("车载终端协议V1.1.4", commandListList);
	}
*/	
}
