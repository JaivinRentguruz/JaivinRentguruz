package com.abel.app.b2b.model.base;

import java.io.Serializable;

public class AuditLogModel  implements Serializable {

    public int id,companyId,auditType,auditFor,createdBy,createdDate;

}
