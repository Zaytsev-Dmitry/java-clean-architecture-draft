package com.clean.architecture.application.prepared.post;

import com.clean.architecture.application.prepared.base.BusinessBasePrepareData;
import com.generated.swaggerCodegen.model.PostCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCreatePrepare implements BusinessBasePrepareData<PostCreateRequest> {

    @Override
    public PostCreateRequest prepare(final PostCreateRequest input) {
        //вызов логики подготовки данных
        return input;
    }

    @Override
    public String getName() {
        return PostCreateRequest.class.getSimpleName();
    }
}
