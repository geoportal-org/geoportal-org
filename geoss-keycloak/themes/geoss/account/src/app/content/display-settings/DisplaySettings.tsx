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
import {
    ActionGroup,
    Button,
    Form,
    FormGroup,
    PageSection,
    PageSectionVariants, TextInput,
} from '@patternfly/react-core';

import { HttpResponse } from '../../account-service/account.service';
import { AccountServiceContext } from '../../account-service/AccountServiceContext';
import { Features } from '../../widgets/features';
import { Msg } from '../../widgets/Msg';
import { ContentPage } from '../ContentPage';
import { ContentAlert } from '../ContentAlert';
import { LocaleSelector } from '../../widgets/LocaleSelectors';

declare const features: Features;
declare const locale: string;

interface DisplaySettingsProps {
}

interface FormFields {
    attributes?: { locale?: [string], zoneinfo?: [string] };
}

interface DisplaySettingsState {
    readonly errors: FormFields;
    readonly formFields: FormFields;
}

/**
 * @author Stan Silvert ssilvert@redhat.com (C) 2018 Red Hat Inc.
 */
export class DisplaySettings extends React.Component<DisplaySettingsProps, DisplaySettingsState> {
    static contextType = AccountServiceContext;
    context: React.ContextType<typeof AccountServiceContext>;
    private readonly DEFAULT_STATE: DisplaySettingsState = {
        errors: {
        },
        formFields: {
            attributes: {}
        }
    };

    public state: DisplaySettingsState = this.DEFAULT_STATE;

    public constructor(props: DisplaySettingsProps, context: React.ContextType<typeof AccountServiceContext>) {
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
                    formFields!.attributes = { locale: [locale] };
                }
                else if (!formFields!.attributes.locale) {
                    formFields!.attributes.locale = [locale];
                }

                this.setState({...{ formFields: formFields as FormFields }});
            });
    }

    private handleCancel = (): void => {
        this.fetchPersonalInfo();
    }

    private handleChange = (value: string, event: React.FormEvent<HTMLInputElement>) => {
        const target = event.currentTarget;
        const name = target.name;

        this.setState({
            errors: { ...this.state.errors, [name]: target.validationMessage },
            formFields: { ...this.state.formFields, [name]: value }
        });
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
                    if (locale !== this.state.formFields.attributes!.locale![0]) {
                        window.location.reload();
                    }
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
                title="displaySettingsHtmlTitle"
                introMessage="displaySettingsSubMessage"
            >
                <PageSection isFilled variant={PageSectionVariants.light}>
                    <Form
                        onSubmit={(event) => this.handleSubmit(event)}
                        className="display-settings-form"
                    >
                        {features.isInternationalizationEnabled && (
                            <FormGroup
                                label={Msg.localize("selectLocale")}
                                isRequired
                                fieldId="locale"
                            >
                                <LocaleSelector
                                    id="locale-selector"
                                    value={fields.attributes!.locale || ""}
                                    onChange={(value) =>
                                        this.setState({
                                            errors: this.state.errors,
                                            formFields: {
                                                ...this.state.formFields,
                                                attributes: {
                                                    ...this.state.formFields.attributes,
                                                    locale: [value],
                                                },
                                            },
                                        })
                                    }
                                />
                            </FormGroup>
                        )}
                        {features.isInternationalizationEnabled && (
                                <FormGroup
                                        label={Msg.localize("timeZone")}
                                        fieldId="timeZone"
                                >
                                    <TextInput
                                            isRequired
                                            type="text"
                                            id="zoneinfo"
                                            name="zoneinfo"
                                            maxLength={254}
                                            value={fields.attributes!.zoneinfo?.[0]}
                                            onChange={(value) =>
                                                    this.setState({
                                                        errors: this.state.errors,
                                                        formFields: {
                                                            ...this.state.formFields,
                                                            attributes: {
                                                                ...this.state.formFields.attributes,
                                                                zoneinfo: [value],
                                                            },
                                                        },
                                                    })
                                            }
                                    ></TextInput>
                                </FormGroup>
                        )}
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
