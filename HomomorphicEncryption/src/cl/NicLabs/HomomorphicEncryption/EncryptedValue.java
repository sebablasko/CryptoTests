package cl.NicLabs.HomomorphicEncryption;

import java.math.BigInteger;
import java.security.SecureRandom;

import android.util.Log;

public class EncryptedValue {

	private BigInteger y1;
	private BigInteger y2;
	//EncryptionParameters values needed
	private BigInteger n;
	private int s;

	private EncryptedValue(BigInteger y1, BigInteger y2, BigInteger n, int s) {
		this.y1 = y1;
		this.y2 = y2;
		this.n = n;
		this.s = s;
	}

	public EncryptedValue(BigInteger value, EncryptionParameters param) {
		Log.d("EncryptedValue", "Encriptacion de valor: " + value);
		BigInteger anillo = param.getAnillo();
		SecureRandom rnd = new SecureRandom();
		BigInteger r = new BigInteger(anillo.bitLength(), rnd);

		BigInteger u = param.n.add(BigInteger.ONE);
		BigInteger s1 = u.modPow(value, anillo);
		BigInteger s2 = param.getPublicKey().modPow(r, anillo);
		this.y1 = param.g.modPow(r, anillo);
		this.y2 = (s2.multiply(s1)).mod(anillo);

		// this.param = param;
		this.n = param.n;
		this.s = param.s;
	}

	public EncryptedValue addValue(EncryptedValue value2) {
		Log.d("EncryptedValue",
				"Calculo suma de valores (Multiplicacion de Encriptados)");
		BigInteger first = this.y1.multiply(value2.y1);
		BigInteger second = this.y2.multiply(value2.y2);
		return new EncryptedValue(first, second, this.n, this.s);
	}

	public EncryptedValue scale(BigInteger factor) {
		Log.d("EncryptedValue",
				"Calculo multiplicacion por escalar (Calculo exponente de encriptados)");
		BigInteger first = this.y1.modPow(factor, this.getAnillo());
		BigInteger second = this.y2.modPow(factor, this.getAnillo());
		return new EncryptedValue(first, second, this.n, this.s);
	}

	public BigInteger decrypt(BigInteger secretKey) {
		Log.d("EncryptedValue", "Desencriptacion");
		BigInteger anillo = this.getAnillo();

		BigInteger ret = this.y1.modPow(secretKey.negate(), anillo);
		ret = ret.multiply(this.y2);
		ret = ret.mod(anillo);
		return solveExponent(ret, this.n, this.s);
	}

	private BigInteger solveExponent(BigInteger wm, BigInteger n, int s) {
		BigInteger t1, t2, i = BigInteger.ZERO;
		for (int j = 1; j < s + 1; j++) {
			t1 = MathUtils.D(n, wm.mod(n.pow(j + 1)));
			t2 = i;
			for (int k = 2; k < j + 1; k++) {
				i = i.subtract(BigInteger.ONE);
				t2 = (t2.multiply(i)).mod(n.pow(j));
				t1 = t1.subtract((t2.multiply(n.pow(k - 1)))
						.divide(new BigInteger(MathUtils.factorial(k) + "")));
			}
			i = t1;
		}
		return i;
	}
	
	private BigInteger getAnillo(){
		return this.n.pow(this.s + 1);
	}

}
