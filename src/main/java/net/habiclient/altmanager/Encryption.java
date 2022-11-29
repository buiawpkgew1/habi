/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.habiclient.altmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import net.minecraft.util.Util;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.habiclient.util.json.JsonException;
import net.habiclient.util.json.JsonUtils;
import net.habiclient.util.json.WsonArray;
import net.habiclient.util.json.WsonObject;

public final class Encryption
{
	private static final String CHARSET = "UTF-8";
	
	private final Cipher encryptCipher;
	private final Cipher decryptCipher;
	
	public Encryption(Path encFolder) throws IOException
	{
		createEncryptionFolder(encFolder);
		
		KeyPair rsaKeyPair =
			getRsaKeyPair(encFolder.resolve("wurst_rsa_public.txt"),
				encFolder.resolve("wurst_rsa_private.txt"));
		
		SecretKey aesKey =
			getAesKey(encFolder.resolve("wurst_aes.txt"), rsaKeyPair);
		
		try
		{
			encryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");
			encryptCipher.init(Cipher.ENCRYPT_MODE, aesKey,
				new IvParameterSpec(aesKey.getEncoded()));
			
			decryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");
			decryptCipher.init(Cipher.DECRYPT_MODE, aesKey,
				new IvParameterSpec(aesKey.getEncoded()));
			
		}catch(GeneralSecurityException e)
		{
			throw new CrashException(
				CrashReport.create(e, "创建AES密码"));
		}
	}
	
	private Path createEncryptionFolder(Path encFolder) throws IOException
	{
		Files.createDirectories(encFolder);
		if(Util.getOperatingSystem() == Util.OperatingSystem.WINDOWS)
			Files.setAttribute(encFolder, "dos:hidden", true);
		
		Path readme = encFolder.resolve("我真的很重要请读我.txt");
		String readmeText = "不要将该文件夹内所有文件分享给任何人!\r\n这里拥有加密的钥匙以防止其他人能够读取你的账户信息.\r\n如果有人找你要求你提供这个文件夹，千万不要给100%是向骗取你的账户.\r\n\r\n不要编辑，重命名，删除这些文件! (除非你知道你再做什么)\r\n如果你做了，则Wurst账户管理器将无法再次识别你所添加的账户，就会变成空白一片.\r\n换句话说，你的账户列表会被清空.";
		Files.write(readme, readmeText.getBytes("UTF-8"),
			StandardOpenOption.CREATE);
		
		return encFolder;
	}
	
	public byte[] decrypt(byte[] bytes)
	{
		try
		{
			return decryptCipher.doFinal(Base64.getDecoder().decode(bytes));
			
		}catch(IllegalArgumentException | GeneralSecurityException e)
		{
			throw new CrashException(CrashReport.create(e, "解密字节"));
		}
	}
	
	public String loadEncryptedFile(Path path) throws IOException
	{
		try
		{
			return new String(decrypt(Files.readAllBytes(path)), CHARSET);
			
		}catch(CrashException e)
		{
			throw new IOException(e);
		}
	}
	
	public JsonElement parseFile(Path path) throws IOException, JsonException
	{
		try(BufferedReader reader = Files.newBufferedReader(path))
		{
			return JsonParser.parseString(loadEncryptedFile(path));
			
		}catch(JsonParseException e)
		{
			throw new JsonException(e);
		}
	}
	
	public WsonArray parseFileToArray(Path path)
		throws IOException, JsonException
	{
		JsonElement json = parseFile(path);
		
		if(!json.isJsonArray())
			throw new JsonException();
		
		return new WsonArray(json.getAsJsonArray());
	}
	
	public WsonObject parseFileToObject(Path path)
		throws IOException, JsonException
	{
		JsonElement json = parseFile(path);
		
		if(!json.isJsonObject())
			throw new JsonException();
		
		return new WsonObject(json.getAsJsonObject());
	}
	
	public byte[] encrypt(byte[] bytes)
	{
		try
		{
			return Base64.getEncoder().encode(encryptCipher.doFinal(bytes));
			
		}catch(GeneralSecurityException e)
		{
			throw new CrashException(CrashReport.create(e, "加密字节"));
		}
	}
	
	public void saveEncryptedFile(Path path, String content) throws IOException
	{
		try
		{
			Files.write(path, encrypt(content.getBytes(CHARSET)));
			
		}catch(CrashException e)
		{
			throw new IOException(e);
		}
	}
	
	public void toEncryptedJson(JsonObject json, Path path)
		throws IOException, JsonException
	{
		try
		{
			saveEncryptedFile(path, JsonUtils.PRETTY_GSON.toJson(json));
			
		}catch(JsonParseException e)
		{
			throw new JsonException(e);
		}
	}
	
	private KeyPair getRsaKeyPair(Path publicFile, Path privateFile)
	{
		if(Files.notExists(publicFile) || Files.notExists(privateFile))
			return createRsaKeys(publicFile, privateFile);
		
		try
		{
			return loadRsaKeys(publicFile, privateFile);
			
		}catch(GeneralSecurityException | ReflectiveOperationException
			| IOException e)
		{
			System.err.println("无法加载RSA密钥对!");
			e.printStackTrace();
			
			return createRsaKeys(publicFile, privateFile);
		}
	}
	
	private SecretKey getAesKey(Path path, KeyPair pair)
	{
		if(Files.notExists(path))
			return createAesKey(path, pair);
		
		try
		{
			return loadAesKey(path, pair);
			
		}catch(GeneralSecurityException | IOException e)
		{
			System.err.println("无法加载AES密钥!");
			e.printStackTrace();
			
			return createAesKey(path, pair);
		}
	}
	
	private KeyPair createRsaKeys(Path publicFile, Path privateFile)
	{
		try
		{
			System.out.println("生成RSA密码.");
			
			// generate keypair
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024);
			KeyPair pair = generator.generateKeyPair();
			
			KeyFactory factory = KeyFactory.getInstance("RSA");
			
			// save public key
			try(ObjectOutputStream out =
				new ObjectOutputStream(Files.newOutputStream(publicFile)))
			{
				RSAPublicKeySpec keySpec = factory.getKeySpec(pair.getPublic(),
					RSAPublicKeySpec.class);
				
				out.writeObject(keySpec.getModulus());
				out.writeObject(keySpec.getPublicExponent());
			}
			
			// save private key
			try(ObjectOutputStream out =
				new ObjectOutputStream(Files.newOutputStream(privateFile)))
			{
				RSAPrivateKeySpec keySpec = factory
					.getKeySpec(pair.getPrivate(), RSAPrivateKeySpec.class);
				
				out.writeObject(keySpec.getModulus());
				out.writeObject(keySpec.getPrivateExponent());
			}
			
			return pair;
			
		}catch(GeneralSecurityException | IOException e)
		{
			throw new CrashException(
				CrashReport.create(e, "创建RSA密钥对"));
		}
	}
	
	private SecretKey createAesKey(Path path, KeyPair pair)
	{
		try
		{
			System.out.println("生成AES密钥.");
			
			// generate key
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			keygen.init(128);
			SecretKey key = keygen.generateKey();
			
			// save key
			Cipher rsaCipher = Cipher.getInstance("RSA");
			rsaCipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());
			Files.write(path, rsaCipher.doFinal(key.getEncoded()));
			
			return key;
			
		}catch(GeneralSecurityException | IOException e)
		{
			throw new CrashException(CrashReport.create(e, "创建AES密钥"));
		}
	}
	
	private KeyPair loadRsaKeys(Path publicFile, Path privateFile)
		throws GeneralSecurityException, ReflectiveOperationException,
		IOException
	{
		KeyFactory factory = KeyFactory.getInstance("RSA");
		
		// load public key
		PublicKey publicKey;
		try(ObjectInputStream in =
			new ObjectInputStream(Files.newInputStream(publicFile)))
		{
			publicKey = factory.generatePublic(new RSAPublicKeySpec(
				(BigInteger)in.readObject(), (BigInteger)in.readObject()));
		}
		
		// load private key
		PrivateKey privateKey;
		try(ObjectInputStream in =
			new ObjectInputStream(Files.newInputStream(privateFile)))
		{
			privateKey = factory.generatePrivate(new RSAPrivateKeySpec(
				(BigInteger)in.readObject(), (BigInteger)in.readObject()));
		}
		
		return new KeyPair(publicKey, privateKey);
	}
	
	private SecretKey loadAesKey(Path path, KeyPair pair)
		throws GeneralSecurityException, IOException
	{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());
		
		return new SecretKeySpec(cipher.doFinal(Files.readAllBytes(path)),
			"AES");
	}
}
