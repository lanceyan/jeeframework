package com.jeeframework.logicframework.util.server.tcp.protocol;

import com.jeeframework.util.io.BSPkgHead;
import com.jeeframework.util.io.JeeSerializable;

public abstract class NetData implements JeeSerializable {
	private BSPkgHead pkgHead = null;
	protected long bornTime = System.currentTimeMillis();

	protected long result = 0;

	public long getResult() {
		return result;
	}

	public void setResult(long result) {
		this.result = result;
	}

	public long getBornTime() {
		return bornTime;
	}

	public long getLiveTime() {
		return System.currentTimeMillis() - bornTime;
	}

	public abstract long getCmdId();

	public BSPkgHead getPkgHead() {
		return pkgHead;
	}

	public void setPkgHead(BSPkgHead pkgHead) {
		this.pkgHead = pkgHead;
	}
}
