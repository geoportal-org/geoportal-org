/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import * as React from 'react';
import { ActionGroup,
    Button,
    Form,
    FormGroup,
    TextInput,
    PageSection,
    PageSectionVariants,
} from '@patternfly/react-core';

import { HttpResponse } from '../../account-service/account.service';
import { AccountServiceContext } from '../../account-service/AccountServiceContext';
import { Msg } from '../../widgets/Msg';
import { ContentPage } from '../ContentPage';
import { ContentAlert } from '../ContentAlert';

interface CustomFieldsProps {
}

interface FormFields {
    attributes?: { greeting?: [string], organization?: [string], job_title?: [string], opacity?: [number] };
}

interface CustomFieldsState {
    readonly errors: FormFields;
    readonly formFields: FormFields;
}

/**
 * @author Stan Silvert ssilvert@redhat.com (C) 2018 Red Hat Inc.
 */
export class CustomFields extends React.Component<CustomFieldsProps, CustomFieldsState> {
    static contextType = AccountServiceContext;
    context: React.ContextType<typeof AccountServiceContext>;
    private readonly DEFAULT_STATE: CustomFieldsState = {
        errors: {
        },
        formFields: {
            attributes: {}
        }
    };

    public state: CustomFieldsState = this.DEFAULT_STATE;

    public constructor(props: CustomFieldsProps, context: React.ContextType<typeof AccountServiceContext>) {
        super(props);
        this.context = context;

        this.fetchPersonalInfo();
    }

    private fetchPersonalInfo(): void {
        this.context!.doGet<FormFields>("/")
            .then((response: HttpResponse<FormFields>) => {
                this.setState(this.DEFAULT_STATE);
                const formFields = response.data;
                if (!formFields!.attributes) {
                    formFields!.attributes = { opacity: [100] };
                }
                else if (!formFields!.attributes.opacity) {
                    formFields!.attributes.opacity = [100];
                }

                this.setState({...{ formFields: formFields as FormFields }});
            });
    }

    private handleCancel = (): void => {
        this.fetchPersonalInfo();
    }

    private handleSubmit = (event: React.FormEvent<HTMLFormElement>): void => {
        event.preventDefault();
        const form = event.target as HTMLFormElement;
        const isValid = form.checkValidity();
        if (isValid) {
            const reqData: FormFields = { ...this.state.formFields };
            this.context!.doPost<void>("/", reqData)
                .then(() => {
                    ContentAlert.success('accountUpdatedMessage');
                });
        } else {
            const formData = new FormData(form);
            const validationMessages = Array.from(formData.keys()).reduce((acc, key) => {
                acc[key] = form.elements[key].validationMessage
                return acc
            }, {});
            this.setState({
                errors: { ...validationMessages },
                formFields: this.state.formFields
            });
        }
    }

    public render(): React.ReactNode {
        const fields: FormFields = this.state.formFields;
        return (
            <ContentPage
                title="customFieldsHtmlTitle"
                introMessage="customFieldsSubMessage"
            >
                <PageSection isFilled variant={PageSectionVariants.light}>
                    <Form
                        onSubmit={(event) => this.handleSubmit(event)}
                        className="custom-fields-form"
                    >
                        {<FormGroup
                                label={Msg.localize("greeting")}
                                fieldId="greeting"
                        >
                            <TextInput
                                    isRequired
                                    type="text"
                                    id="greeting"
                                    name="greeting"
                                    maxLength={254}
                                    value={fields.attributes!.greeting?.[0]}
                                    onChange={(value) =>
                                            this.setState({
                                                errors: this.state.errors,
                                                formFields: {
                                                    ...this.state.formFields,
                                                    attributes: {
                                                        ...this.state.formFields.attributes,
                                                        greeting: [value],
                                                    },
                                                },
                                            })
                                    }
                            ></TextInput>
                        </FormGroup>
                        }
                        {<FormGroup
                                label={Msg.localize("job_title")}
                                fieldId="job_title"
                        >
                            <TextInput
                                    isRequired
                                    type="text"
                                    id="job_title"
                                    name="job_title"
                                    maxLength={254}
                                    value={fields.attributes!.job_title?.[0]}
                                    onChange={(value) =>
                                            this.setState({
                                                errors: this.state.errors,
                                                formFields: {
                                                    ...this.state.formFields,
                                                    attributes: {
                                                        ...this.state.formFields.attributes,
                                                        job_title: [value],
                                                    },
                                                },
                                            })
                                    }
                            ></TextInput>
                        </FormGroup>
                        }
                        {<FormGroup
                                label={Msg.localize("organization")}
                                fieldId="organization"
                        >
                            <TextInput
                                    isRequired
                                    type="text"
                                    id="organization"
                                    name="organization"
                                    maxLength={254}
                                    value={fields.attributes!.organization?.[0]}
                                    onChange={(value) =>
                                            this.setState({
                                                errors: this.state.errors,
                                                formFields: {
                                                    ...this.state.formFields,
                                                    attributes: {
                                                        ...this.state.formFields.attributes,
                                                        organization: [value],
                                                    },
                                                },
                                            })
                                    }
                            ></TextInput>
                        </FormGroup>
                        }
                        {<FormGroup
                                label={Msg.localize("opacity")}
                                fieldId="opacity"
                            >
                                <TextInput
                                    isRequired
                                    type="number"
                                    id="opacity"
                                    name="opacity"
                                    maxLength={254}
                                    value={fields.attributes!.opacity?.[0]}
                                    onChange={(value) =>
                                            this.setState({
                                                errors: this.state.errors,
                                                formFields: {
                                                    ...this.state.formFields,
                                                    attributes: {
                                                        ...this.state.formFields.attributes,
                                                        opacity: [Number(value)],
                                                    },
                                                },
                                            })
                                    }
                                ></TextInput>
                            </FormGroup>
                        }
                        <ActionGroup>
                            <Button
                                type="submit"
                                id="save-btn"
                                variant="primary"
                                isDisabled={
                                    Object.values(this.state.errors).filter((e) => e !== "")
                                    .length !== 0
                                }
                            >
                                <Msg msgKey="doSave" />
                            </Button>
                            <Button
                                id="cancel-btn"
                                variant="link"
                                onClick={this.handleCancel}
                            >
                                <Msg msgKey="doCancel" />
                            </Button>
                        </ActionGroup>
                    </Form>
            </PageSection>
        </ContentPage>
        );
    }
}
