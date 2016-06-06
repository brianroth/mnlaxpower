require 'rails_helper'
require 'shared_examples_for_models'

describe Organization do
  let(:name) { 'Eagan' }

  describe '#save' do
    subject do
      organization = Organization.create!(name: name)
    end

    it { is_expected.to have_attributes name: name }
    it_behaves_like 'a newly created model instance'
  end

  describe '#errors' do
    subject do
      organization.validate
      organization.errors
    end

    context 'when validation passes' do
      let(:organization) { Organization.create(name: name) }

      it { should be_empty }
    end

    context 'when validation fails' do
      context 'when name is missing' do
        let(:organization) { Organization.new() }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:name, "can't be blank"]
        end
      end

      context 'when name is a duplicate' do
        let(:organization) { Organization.new(name: name) }

        before do
          Organization.create(name: name)
        end

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:name, "has already been taken"]
        end
      end
    end
  end
end
