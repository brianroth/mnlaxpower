require 'rails_helper'
require 'shared_examples_for_models'

describe Season do
  let(:name) {'Boys Summer 2016'}
  let(:cms_code) { 12179 }

  describe '#save' do
    subject do
      season = Season.create!(name: name, cms_code: cms_code)
    end

    it { is_expected.to have_attributes name: name }
    it { is_expected.to have_attributes cms_code: cms_code }
    it { is_expected.to have_attributes default: false }
    it_behaves_like 'a newly created model instance'
  end

  describe '#errors' do
    subject do
      season.validate
      season.errors
    end

    context 'when validation passes' do
      let(:season) { Season.create(name: name, cms_code: cms_code) }

      it { should be_empty }
    end

    context 'when validation fails' do
      context 'when name is missing' do
        let(:season) { Season.create(cms_code: cms_code) }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:name, "can't be blank"]
        end
      end

      context 'when name is a duplicate' do
        let(:season) { Season.new(name: name, cms_code: 123) }

        before do
          Season.create(name: name, cms_code: 234)
        end

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:name, "has already been taken"]
        end
      end

      context 'when cms_code is missing' do
        let(:season) { Season.create(name: name) }

        it 'communicates the validation error' do
          expect(subject.count).to be 1
          expect(subject.first).to eq [:cms_code, "can't be blank"]
        end
      end
    end
  end
end
