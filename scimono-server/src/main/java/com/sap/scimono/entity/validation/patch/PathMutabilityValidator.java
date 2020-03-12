
package com.sap.scimono.entity.validation.patch;

import com.sap.scimono.callback.schemas.SchemasCallback;
import com.sap.scimono.entity.patch.PatchOperation;
import com.sap.scimono.entity.schema.Attribute;
import com.sap.scimono.entity.validation.Validator;
import com.sap.scimono.helper.Strings;

public class PathMutabilityValidator implements Validator<PatchOperation> {

  private final SchemasCallback schemaAPI;

  public PathMutabilityValidator(final SchemasCallback schemaAPI) {
    this.schemaAPI = schemaAPI;
  }

  @Override
  public void validate(final PatchOperation operation) {
    String path = operation.getPath();

    if (isPathRepresentSchema(path)) {
      return;
    }

    Attribute targetAttribute = schemaAPI.getAttribute(path);
    Validator<Attribute> mutabilityValidator = new PatchAttributeMutabilityValidator(PatchOperation.Type.REPLACE.equals(operation.getOp()));

    mutabilityValidator.validate(targetAttribute);
  }

  private boolean isPathRepresentSchema(final String path) {
    return Strings.isNullOrEmpty(path) || schemaAPI.getSchema(path) != null;
  }
}
