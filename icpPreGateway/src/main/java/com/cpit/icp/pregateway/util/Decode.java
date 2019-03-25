package com.cpit.icp.pregateway.util;

public class Decode {
    //签到报文
	public static boolean is0x10(byte[] Receive) {
		// 解析报文(区分版本)
		Byte version = Receive[3];
		if ((version.equals((byte) 0x35))) {
			return (Receive[22] == 0x1b) ? true : false;
		} else {
			return (Receive[5] == 0x10) ? true : false;
		}
	}

	// 重启应答报文
	public static boolean is0x53(byte[] Receive) {
		// 解析报文(区分版本)
		Byte version = Receive[3];
		if ((version.equals((byte) 0x35))) {
			return (Receive[22] == 0x53) ? true : false;
		} else {
			return (Receive[5] == 0x53) ? true : false;
		}
	}
	
	//心跳报文
	public static boolean is0x58(Byte version,byte[] Receive) {
		// 解析报文(区分版本)
		if ((version.equals((byte) 0x35))) {
			return (Receive[22] == 0x58) ? true : false;
		} else {
			return (Receive[5] == 0x58) ? true : false;
		}
	}

	public static String getVersion(byte version) {
		// 解析报文版本
		return "0x" + CommFunction.byteToHexStr2(version);

	}

	public static String getCommand(byte[] Receive) {
		// 解析报文(区分版本)
		Byte version = Receive[3];
		if ((version.equals((byte) 0x35))) {
			return "0x" + CommFunction.byteToHexStr2(Receive[22]);
		} else {
			return "0x" + CommFunction.byteToHexStr2(Receive[5]);
		}
	}

	// 校验报文格式
	/**
	 * @param Receive
	 *            接受报文
	 * @param key
	 * @param equipName
	 *            终端名称
	 * @return
	 */
	public static String DecodePackageStyle(Byte version, byte[] Receive) {

		int eqipDatalen = 0;// 数据域的长度
		byte OrderCode = 0;// 命令代码域
		byte[] equipmentData = null;// 数据域

		// 校验报文格式
		StringBuffer info = new StringBuffer();
		int templen;

		switch (version) {
		case (byte) 0x80:
			if (Receive[0] != -6 || Receive[1] != -11) {
				info.append(MessageInfo.validate_head);
				break;
			}
			// 73、78、79报文不校验 直接给业务网关
			if ("79".equals(CommFunction.byteToHexStr2(Receive[5]))) {
				break;
			}
			if ("73".equals(CommFunction.byteToHexStr2(Receive[5]))) {
				break;
			}
			if ("78".equals(CommFunction.byteToHexStr2(Receive[5]))) {
				break;
			}
			// 判断报文长度是否正确
			if (Receive.length < 7) {
				info.append(MessageInfo.validate_lenght);
				break;
			}
			// 判断长度域是否正确 35报文如长度域为FF,则代表具体的长度域需按照实际数据域的字节数进行计算；
			templen = CommFunction.unsignByteToInt(Receive[2]);
			if ((templen != Receive.length - 4) && (templen !=255)) {
				info.append(MessageInfo.validate_lenghtDomain);
				break;
			}
			// 判断检验和是否正确
			byte[] tempChecksum = new byte[Receive.length - 6];
			ArraysN.copy(tempChecksum, 0, Receive, 5, Receive.length - 6);
			if (!(CommFunction.byteToHexStr2(Receive[Receive.length - 1])
					.equalsIgnoreCase(CommFunction.SumCheck2(tempChecksum)))) {
				info.append(MessageInfo.validate_checkSum);
				break;
			}
			OrderCode = Receive[5];
			eqipDatalen = templen - 3;
			equipmentData = CommFunction.subBytes(tempChecksum, 1, tempChecksum.length - 1);
			break;
		case (byte) 0x35:
			if (Receive[0] != -6 || Receive[1] != -11) {
				info.append(MessageInfo.validate_head);
				break;
			}
			// 7D报文不校验 直接给业务网关
			if ("7D".equals(CommFunction.byteToHexStr2(Receive[22]))) {
				break;
			}
			// 判断报文长度是否正确
			if (Receive.length < 24) {
				info.append(MessageInfo.validate_lenght);
				break;
			}
			// 判断长度域是否正确
			templen = CommFunction.unsignByteToInt(Receive[2]);
			if (templen != Receive.length - 4) {
				info.append(MessageInfo.validate_lenghtDomain);
				break;
			}
			// 判断检验和是否正确
			byte[] tempChecksum2 = new byte[Receive.length - 23];
			ArraysN.copy(tempChecksum2, 0, Receive, 22, Receive.length - 23);
			if (!(CommFunction.byteToHexStr2(Receive[Receive.length - 1])
					.equalsIgnoreCase(CommFunction.SumCheck2(tempChecksum2)))) {
				info.append(MessageInfo.validate_checkSum);
				break;
			}
			OrderCode = Receive[22];
			eqipDatalen = templen - 20;
			equipmentData = CommFunction.subBytes(tempChecksum2, 1, tempChecksum2.length - 1);
			break;
		default:
			info.append(MessageInfo.validate_version);
			break;
		}
		return info.toString();
	}

	public static String Decode10Package(byte[] Receive) {

		// 解析报文(区分版本)
		Byte version = Receive[3];
		int index = 6;// 数据域起始
		if ((version.equals((byte) 0x35))) {
			index = 23;
		}
		byte[] des1 = new byte[8];
		ArraysN.copy(des1, 0, Receive, index, 8);
		return CommFunction.bytesToHexString(des1);

	}
	/**
	 * 获取0x05、0x6C、0x15、0x7A报文的用户id和指令序号
	 * @param Receive
	 * @return
	 */
	public static String getidandSN(byte[] Receive) {
		// 解析报文(区分版本)
		Byte version = Receive[3];
		byte[] des1 = new byte[2];
		if ((version.equals((byte) 0x35))) {
			ArraysN.copy(des1, 0, Receive, 23, 2);
		} else {
			ArraysN.copy(des1, 0, Receive, 6, 2);			
		}
		return CommFunction.bytesToHexString(des1);
	}
	
	/**
	 * 获取0x79的中心交易流水号
	 * @param Receive
	 * @return
	 */
	public static String getTransactionFlowing (byte[] Receive) {
		// 解析报文(区分版本)
		Byte version = Receive[3];
		byte[] des1 = new byte[15];
//		if ((version.equals((byte) 0x35))) {
//			ArraysN.copy(des1, 0, Receive, 23, 2);
//		} else {
			ArraysN.copy(des1, 0, Receive, 27, 15);				
//		}
		return CommFunction.bytesToHexString(des1);
	}
	// 包头加设备编码
	// public static byte[] addCode(byte[] Receive) {
	//
	// // 解析报文(区分版本)
	// Byte version = Receive[3];
	// int index = 6;// 数据域起始
	// if ((version.equals((byte) 0x35))) {
	// index = 23;
	// }
	// byte[] des1 = new byte[8];
	// ArraysN.copy(des1, 0, Receive, index, 8);
	// return ArraysN.addBytes(des1,Receive);
	//
	// }

}
