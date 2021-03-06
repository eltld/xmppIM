package com.quinn.xmpp.core.launch;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.quinn.xmpp.R;
import com.quinn.xmpp.smack.Smack;

public class LoginTask extends AsyncTask<String, Integer, Boolean> {

	private Smack smack;
	private Resources res;

	public LoginTask(Resources res,Smack smack) {
		this.res = res;
		this.smack = smack;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		boolean rest = smack.login(params[0], params[1]);
		VCard vCard = new VCard();
		Bitmap bm = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
		vCard.setAvatar(Bitmap2Bytes(bm));
		
		try {

			vCard.save(smack.getConnection());
			System.out.println("avator length = " + vCard.getAvatar());
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return rest;
	}

	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

}
